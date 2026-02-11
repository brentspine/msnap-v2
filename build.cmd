@echo off
set "starttime=%time%"
echo "Building contracts..."
call mvn -pl contracts clean install
echo "Getting OpenAPI api-docs via Test"
call mvn test -pl api-service -Dtest=OpenApiGeneratorTest
echo "Building API service..."
call mvn -pl api-service clean install -DskipTests
echo "Generating API client code..."
call mvn -pl api-client clean install -DskipTests
echo "Done!"
set "endtime=%time%"
:: Calculate elapsed time using PowerShell
powershell -Command "$s = '%starttime%'.Replace(',', '.'); $e = '%endtime%'.Replace(',', '.'); $start = [DateTime]::Parse($s); $end = [DateTime]::Parse($e); if ($end -lt $start) { $end = $end.AddDays(1) }; $diff = $end - $start; Write-Host '----------------------------------------'; Write-Host ('Total Time Elapsed: {0:hh\:mm\:ss}' -f $diff); Write-Host '----------------------------------------'"
