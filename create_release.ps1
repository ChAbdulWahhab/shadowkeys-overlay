# PowerShell script to create GitHub release using GitHub API
# Requires: GitHub Personal Access Token with repo scope

param(
    [string]$Token = "",
    [string]$Repo = "ChAbdulWahhab/shadowkeys-overlay",
    [string]$Tag = "v1.2.1",
    [string]$APKPath = "release/ShadowKeys.apk"
)

if (-not $Token) {
    Write-Host "❌ Error: GitHub Personal Access Token required" -ForegroundColor Red
    Write-Host "Usage: .\create_release.ps1 -Token 'your_github_token'" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "To get a token:" -ForegroundColor Cyan
    Write-Host "1. Go to https://github.com/settings/tokens" -ForegroundColor Cyan
    Write-Host "2. Generate new token (classic)" -ForegroundColor Cyan
    Write-Host "3. Select 'repo' scope" -ForegroundColor Cyan
    exit 1
}

$ReleaseNotesFile = if ($Tag -eq "v1.2.0") { "RELEASE_NOTES_v1.2.0.md" } else { "RELEASE_NOTES_v1.2.1.md" }
$ReleaseNotes = Get-Content $ReleaseNotesFile -Raw

# Create release
    $ReleaseBody = @{
        tag_name = $Tag
        name = "ShadowKeys $Tag - GTA SA Cheat Injector"
        body = $ReleaseNotes
    draft = $false
    prerelease = $false
} | ConvertTo-Json

Write-Host "📦 Creating GitHub release..." -ForegroundColor Cyan

try {
    $Headers = @{
        "Authorization" = "token $Token"
        "Accept" = "application/vnd.github.v3+json"
    }
    
    $CreateResponse = Invoke-RestMethod -Uri "https://api.github.com/repos/$Repo/releases" `
        -Method Post `
        -Headers $Headers `
        -Body $ReleaseBody `
        -ContentType "application/json"
    
    $UploadUrl = $CreateResponse.upload_url -replace '\{.*$', ''
    $ReleaseId = $CreateResponse.id
    
    Write-Host "✅ Release created successfully! (ID: $ReleaseId)" -ForegroundColor Green
    
    # Upload APK
    if (Test-Path $APKPath) {
        Write-Host "📤 Uploading APK..." -ForegroundColor Cyan
        
        $APKName = Split-Path $APKPath -Leaf
        $APKBytes = [System.IO.File]::ReadAllBytes((Resolve-Path $APKPath))
        $APKBase64 = [System.Convert]::ToBase64String($APKBytes)
        
        $UploadHeaders = @{
            "Authorization" = "token $Token"
            "Accept" = "application/vnd.github.v3+json"
            "Content-Type" = "application/vnd.android.package-archive"
        }
        
        $UploadUrl = "$UploadUrl?name=$APKName"
        
        Invoke-RestMethod -Uri $UploadUrl `
            -Method Post `
            -Headers $UploadHeaders `
            -Body $APKBytes `
            -ContentType "application/vnd.android.package-archive"
        
        Write-Host "✅ APK uploaded successfully!" -ForegroundColor Green
        Write-Host ""
        Write-Host "🎉 Release URL: $($CreateResponse.html_url)" -ForegroundColor Green
    } else {
        Write-Host "⚠️  APK not found at: $APKPath" -ForegroundColor Yellow
    }
    
} catch {
    Write-Host "❌ Error: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.ErrorDetails.Message) {
        Write-Host "Details: $($_.ErrorDetails.Message)" -ForegroundColor Red
    }
    exit 1
}

