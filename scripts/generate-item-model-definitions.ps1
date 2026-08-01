# Generates assets/tools_of_the_gods/items/*.json from legacy model override files.
# Used for Minecraft 1.21.8+ where item model definitions replace ItemProperties.

$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $PSScriptRoot
$modelsDir = Join-Path $root 'common\src\main\resources\tools_of_the_gods\models\item'
$itemsDir = Join-Path $root 'common\src\main\resources\tools_of_the_gods\items'
New-Item -ItemType Directory -Force -Path $itemsDir | Out-Null

function New-ModelRef([string]$modelPath) {
    return @{
        type  = 'minecraft:model'
        model = "tools_of_the_gods:item/$modelPath"
    }
}

function New-TierDispatch([string]$baseModel, [array]$entries) {
    $dispatch = @{
        type     = 'minecraft:range_dispatch'
        property = 'tools_of_the_gods:tier'
        scale    = 1
        fallback = (New-ModelRef $baseModel)
        entries  = @()
    }
    foreach ($entry in $entries) {
        $dispatch.entries += @{
            threshold = [double]$entry.threshold
            model     = (New-ModelRef $entry.model)
        }
    }
    return $dispatch
}

function Write-ItemDefinition([string]$itemId, [hashtable]$definition) {
    $outPath = Join-Path $itemsDir "$itemId.json"
    $json = ($definition | ConvertTo-Json -Depth 12) -replace '(\r?\n)\s+', "`n"
    Set-Content -Path $outPath -Value $json -Encoding UTF8
    Write-Host "Wrote $outPath"
}

$shieldBlocking = 'shield_of_the_gods_blocking'

Get-ChildItem -Path $modelsDir -Filter '*_of_the_gods.json' | ForEach-Object {
    $itemId = $_.BaseName
    $raw = Get-Content -Raw -Path $_.FullName | ConvertFrom-Json
    if (-not $raw.overrides) {
        return
    }

    $tierEntries = @()
    $hasBlocking = $false
    foreach ($override in $raw.overrides) {
        if ($override.predicate.blocking -eq 1) {
            $hasBlocking = $true
            continue
        }
        $tier = $override.predicate.'tools_of_the_gods:tier'
        if ($null -ne $tier) {
            $modelName = ($override.model -replace '^tools_of_the_gods:item/', '')
            $tierEntries += @{ threshold = $tier; model = $modelName }
        }
    }

    if ($tierEntries.Count -eq 0) {
        return
    }

    $tierEntries = $tierEntries | Sort-Object { $_.threshold }
    $tierModel = New-TierDispatch $itemId $tierEntries

    if ($hasBlocking) {
        $definition = @{
            model = @{
                type     = 'minecraft:condition'
                property = 'minecraft:using_item'
                on_true  = (New-ModelRef $shieldBlocking)
                on_false = $tierModel
            }
        }
    }
    else {
        $definition = @{ model = $tierModel }
    }

    Write-ItemDefinition $itemId $definition
}

# Simple wrappers for every other base item model (gems, upgrades, staff, wings, etc.).
Get-ChildItem -Path $modelsDir -Filter '*.json' | ForEach-Object {
    $itemId = $_.BaseName
    if ($itemId -match '_tier\d+$' -or $itemId -match '_blocking$' -or $itemId.StartsWith('_')) {
        return
    }
    $outPath = Join-Path $itemsDir "$itemId.json"
    if (Test-Path $outPath) {
        return
    }
    Write-ItemDefinition $itemId @{ model = (New-ModelRef $itemId) }
}

# Primal tools have no legacy model file; point at the matching god-tier base model.
$primalMap = @{
    'primal_wooden_tools_pickaxe' = 'pickaxe_of_the_gods'
    'primal_wooden_tools_hammer'  = 'hammer_of_the_gods'
    'primal_wooden_tools_axe'     = 'axe_of_the_gods'
    'primal_wooden_tools_shovel'  = 'shovel_of_the_gods'
    'primal_wooden_tools_hoe'     = 'hoe_of_the_gods'
    'primal_wooden_tools_sword'   = 'sword_of_the_gods'
    'primal_wooden_tools_bow'     = 'bow_of_the_gods'
}
foreach ($entry in $primalMap.GetEnumerator()) {
    $outPath = Join-Path $itemsDir "$($entry.Key).json"
    if (Test-Path $outPath) {
        continue
    }
    Write-ItemDefinition $entry.Key @{ model = (New-ModelRef $entry.Value) }
}

Write-Host "Done. Generated item model definitions in $itemsDir"
