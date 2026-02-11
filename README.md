# MSnap v2

The goal of this project is to automatically manage Microsoft Accounts, most prominently ones that own Minecraft and associated Items on Minecraft Servers. This process includes evaluating and securing the accounts by changing passwords, enabling 2FA, and removing any unauthorized access. We store all data that is associated account or that WAS associated. All data should be managable via the API and soon a separate Web Interface.

Things that are deemed valuable:
- Microsoft Account on its own without any associated items: a couple of cents
- Minecraft Account: Depends on the license type. Gamepass is worth way less than a full license. A full license.
- Minecraft Capes: Each cape value is managable, but they can be worth a lot of money. Some capes are worth more than the account itself.
- Server Data:
    - Whether account is banned on a server
    - Playtime on a server
    - Currency on a server
    - Items on a server
    - Ranks on a server
- Minecraft Account Name: Currently only evaluated by length

### Architecture
Each Microsoft Account has:
- Microsoft Account Data
- Minecraft license type
    - Premium
    - Gamepass
        - Then with timestamp when it expires
    - None
- Security Information
    - Recovery Code
    - Email
    - Alias List: List of associated Email addresses
    - Alias Phone Numbers: List of associated Phone Numbers
    - App Password: Generated app password
- Outlook Information:
    - Mails Sent:
        - Another class, that stores the information about mails sent
        - From
        - To
        - Subject
        - Timestamp
        - Body Preview: First 100 characters of the body
    - Inbox:
        - Same class as Mails Sent, but for received mails
- Initial Security Information: The state of the account when it was first obtained
    - Same structure as Security Information but more info
    - MailUrl: can be null, the URL given to access the email inbox
    - MailPassword: can be null, the password to access the email inbox
- Source
- Proxies
- Minecraft Account
    - Can be null if no license is present
    - Active -> Single field that is true, if we have control over the account
    - UUID -> The UUID of the Minecraft Account. Is unique
    - Name -> The name of the account
    - Capes -> List of capes that the account has
    - Creation Date -> When the Minecraft account was created
    - (Not currently) Name History -> List of name changes that the account had
    - Servers -> List of servers that the account has data on. Each Server has its own class.
        - Each entry only stores data that is connected to the specific account and server. Meaning Server Name and Server IP are stored somewhere else.
            - UnbanTimestamp: Timestamp when a ban is lifted (0 or null means not banned)
            - Playtime: Total playtime on the server
            - Rank: The highest rank
            - Items: List of items the account has on the server
            - Each Currency, Rank and Item has an associated value per unit. We can change the value of each unit on the fly via a WebConsole
        - Just implement an example Server, HGLabor with 1 currency: "SMP Money" and a rank with the options {Player, VIP, Developer, Admin}
    - Minecoins: Amount of Minecoins the account has. Is valuable, just as a currency
- AdditionalInfo:
    - First Name
    - Last Name
    - Region
    - Birthday
    - Profile Image
    - PaymentCards: List of payment cards associated with the account.
        - Type: Mastercard, Visa, etc.
        - Display: What is shown when the card is displayed, for example "ahmed alaa eldin elsayed ••2204"
        - Address: For example "suez, suez, 12345, EG"
    - OAuthApps: Don't implement yet
    - MSPoints: Amount of MS Points the account has
    - MSWalletBalance: Amount of money in the MS Wallet
    - MSWalletBalanceCurrency: Currency of the MS Wallet balance
- OriginalAdditionalInfo
- Orders: https://account.microsoft.com/billing/orders?lang=en-GB&period=SevenYears&type=All&refd=account.microsoft.com
    - Address: can be null. Otherwise just a string
    - DisplayName: Just scraped from the order page (plain string)
    - Price: Price paid for the order
    - PriceCurrency: Currency of the price
    - OrderDate: Date of the order, just scraped
    - OrderNumber: Order number, just scraped
    - SystemTriggered: Whether the order was triggered by our system
- Games: We don't need to store more info, just the fact that we own it
- Note: A freetext field that can store any additional information

### Proxy System
- Accounts can have multiple (0-n) proxies associated
- Each Proxy has:
    - IP Address
    - Port
    - Region
    - Username
    - Password
    - ProxyInfo
- ProxyInfo
    - For example for different providers
    - Provider Name
    - Maybe more fields later

### Cape System
- Capes are stored in the db and have a name, description, value and image.
    - Alias: unique key
    - value: The monetary value of the cape we give it
    - display name: The name that is shown to users
    - description: A description of the cape
    - image: An image of the cape for display purposes
    - updated at: When the cape data was last updated
    - allows codes: Whether the cape allows redeemable codes
- We want to store cape codes:
    - Associated with a cape. One cape can have multiple codes
    - code: The redeemable code for the cape
    - source: From where the cape was obtained
    - redeemed_at: When the code was redeemed. Null if not redeemed yet
    - redeemed_by: Key to a minecraft account

### Source class
- Says from where something has been obtained
- One source can be tied to multiple items
- Name: A string that describes the source, for example "Eldorado Purchase {username}"
- Price: Can be 0
- Timestamp: When the data was obtained
- Note: A note that describes the source in more detail, for example "Bought on Eldorado for 10€"

### Game Class
- Represents a game that is owned by the Microsoft Account
- Title: The title of the game
- Value: The monetary value of the game

### Activity System
We want to store all activity of the account with all details. Each activity has:
- Timestamp: When the activity happened
- IsOwn: Whether the activity was performed by us (true) or is suspicious (false)
- Activity Type:
    - Unknown: Should not be used, but is a fallback
    - MSForeignSignIn: When the account signed in. Always isOwn=false. only detected via https://account.live.com/Activity
    - IP Address
    - Device Info: Like Chrome - Windows
    - Location Info
    - List of actions just an array like ["Additional verification requested"]
    - More to be added
    - ChangedDetail: A detail of the account was changed (probably) by us
        - Field
        - Old Value
        - New Value

### Valuation System
We want to be able to value each account. Therefore we need a valuation system that can be adjusted on the fly.
For that we implement a price list system.
We have:
- price_lists(id PK, name, status DRAFT|PUBLISHED|ARCHIVED, created_at, published_at, published_by)
- price_entries(id PK, price_list_id FK, entity_type CAPE|CURRENCY|RANK|ITEM|GAME|LICENSE, entity_key, value, meta_json)
- meta_json examples: { "cap": 1000, "multiplier_if_not_banned": 1.0, "multiplier_if_banned": 0.0 }

An account valuation is then performed by taking the latest PUBLISHED price list and summing up all values for the account based on that price list.

We then have a central evaluation service with potential subclasses that delegate the evaluation of specific entities to specific services, for example CapeValuationService, CurrencyValuationService, etc. This way we can easily manage the valuation of each entity and change it on the fly via the price list system.
Instead of recalculating the value of each account every time we need it, we need to cache the value and only recalculate it when something changes.

### Bounty System
We want to be able to set bounties for specific UUIDs or names
Bounty Class:
 - UUID (nullable)
 - Name (nullable)
 - Value: The value of the bounty
 - ValueMode: Possible Modes: 
   - Overwrite: New value, no matter what
   - Add: Add this value to the existing value
 - Reason: A note that describes the reason for the bounty

### Money System (Currency Conversion)
The standard currency is EUR because its stable but we want to be able to change that on the fly. Therefore we implement a currency conversion system. We have a Money record, that stores the amount and a currency identifier.
We also have a currency conversion table that stores the conversion rates between different currencies. This way we can easily convert the value of an account to the desired currency. The currency conversion table has the following fields:
- currency_key: The identifier of the currency, for example "EUR", "USD", "GBP", etc. Or for crypto: "crypto_BTC", "crypto_ETH", etc.
- conversion_rate_to_eur: The conversion rate to EUR, for example 1.0 for EUR, 1.1 for USD, etc.
- updated_at: When the conversion rate
  We will then be able to edit the values via an external API. Idea:
```java
public record Money(Currency currency, long minor) {

    public static Money zero(Currency currency) {
        return new Money(currency, 0L);
    }

    public Money plus(Money other) {
        requireSameCurrency(other);
        return new Money(currency, Math.addExact(minor, other.minor));
    }

    public Money minus(Money other) {
        requireSameCurrency(other);
        return new Money(currency, Math.subtractExact(minor, other.minor));
    }

    private void requireSameCurrency(Money other) {
        if (currency.equals(other.currency())) { return; }
        throw new IllegalArgumentException("Currency mismatch: " + currency + " vs " + other.currency());
    }
}
```
With a conversion service

## Components

This section just describes what components we need, meaning which broad areas we want to cover.

### Core logic
This is where the entities, evaluation, money services and all the core logic is implemented. This should be as independent as possible from the rest of the system, so that we can easily change the rest of the system without affecting the core logic.

### API
We want to have an API that allows us to manage the accounts and all associated data. This includes creating, updating and deleting accounts, as well as managing the price lists and currency conversion rates. The API should, for now, just be a REST API that can be accessed with a hardcoded API key for authentication. We can later implement a more sophisticated authentication system if needed.

### Web Interface
This is only planned to be implemented AFTER the discord bot, because managing through Discord makes many things way easier. We want to have a web interface that allows us to manage the accounts and all associated data in a more user-friendly way. This includes viewing the accounts, editing them, managing the price lists and currency conversion rates, etc. The web interface should be built on top of the API, so that all logic is still in the core and can be reused for other interfaces if needed. For example we could later implement a Telegram Bot that also uses the API to manage the accounts. The web interface should also have a dashboard that gives us an overview of the accounts, for example how many accounts we have, how many of them are active, what is the total value of all accounts, etc. The dashboard should also have some charts that show the distribution of account values, the distribution of license types, etc. The web interface should also allow us to view the details of each account, including all associated data, and also allow us to edit the account, for example change the note, add a new source, etc. The web interface should also allow us to manage the price lists and currency conversion rates, for example create a new price list, edit an existing price list, etc.

### Discord Bot
A discord bot instead of a more sophisticated web interface for now, because it allows us to easily manage the accounts without having to implement a full web interface. 

### Jobs/Automation
We need automated jobs for certain tasks, for example:
 - Changing security details
 - Redeem Cape Code
 - Getting server data
So 2 types: Selenium based and Mineflayer based

### 2FA and Mail Service
We need to automatically handle mail verification and potentially 2FA apps. 

Important is to remember to lazy fetch data that is not always needed. Otherwise the monolith MicrosoftAccount entity will become way too big and slow to handle. For example Outlook Mails should only be fetched when needed, not always when the account is loaded.
