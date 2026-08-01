# Run Minecraft client for any loader/version subproject.
# Examples:
#   .\scripts\run-client.ps1 fabric 1.20.1
#   .\scripts\run-client.ps1 neoforge 1.21.8
#   .\scripts\run-client.ps1 forge 1.18.2
#   .\scripts\run-client.ps1 -List
param(
    [Parameter(Position = 0)]
    [ValidateSet('fabric', 'neoforge', 'forge', '')]
    [string]$Loader = '',

    [Parameter(Position = 1)]
    [string]$Version = '',

    [switch]$List
)

$root = Split-Path -Parent $PSScriptRoot
Push-Location $root

$known = @{
    fabric   = @('1.16.5', '1.18.2', '1.19.2', '1.20.1', '1.21.1', '1.21.8', '26.1')
    neoforge = @('1.21.1', '1.21.8', '26.1')
    forge    = @('1.18.2', '1.19.2', '1.20.1')
}

if ($List) {
    Write-Host "Available runClient targets:" -ForegroundColor Cyan
    foreach ($entry in $known.GetEnumerator() | Sort-Object Name) {
        foreach ($v in $entry.Value) {
            Write-Host "  $($entry.Key) $v  ->  .\scripts\run-client.ps1 $($entry.Key) $v"
        }
    }
    Pop-Location
    exit 0
}

if (-not $Loader -or -not $Version) {
    Write-Host "Usage: .\scripts\run-client.ps1 <fabric|neoforge|forge> <version>" -ForegroundColor Yellow
    Write-Host "       .\scripts\run-client.ps1 -List" -ForegroundColor Yellow
    Pop-Location
    exit 1
}

if ($Loader -eq 'forge' -and $Version -eq '1.16.5') {
    Write-Host "forge-1.16.5 is not registered yet (no build.gradle). Skipping." -ForegroundColor Red
    Pop-Location
    exit 1
}

if (-not $known[$Loader].Contains($Version)) {
    Write-Host "Unknown ${Loader} version '$Version'. Known: $($known[$Loader] -join ', ')" -ForegroundColor Red
    Pop-Location
    exit 1
}

$project = "${Loader}-${Version}"
$propName = "${Loader}Versions"
Write-Host "Running :${project}:runClient (loader=$Loader version=$Version)..." -ForegroundColor Cyan

& .\gradlew.bat ":${project}:runClient" "-PtogLoader=$Loader" "-P${propName}=$Version" --no-daemon
$code = $LASTEXITCODE
Pop-Location
exit $code
