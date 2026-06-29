$parentDir = "c:\Users\peolu\OneDrive\Desktop\Assignment of React\ASM1"
$sourceDir = "c:\Users\peolu\OneDrive\Desktop\Assignment of React\ASM1\REACT_Assignment01_Op1"
$targetBase = Join-Path $parentDir "REACT_Assignment01"
$targetSub = Join-Path $targetBase "ex1_task1"
$zipFile = Join-Path $parentDir "Student_account_REACT_Assignment01.zip"

Write-Host "Creating target folder: $targetSub"
if (Test-Path $targetBase) { Remove-Item -Recurse -Force $targetBase }
New-Item -ItemType Directory -Force -Path $targetSub | Out-Null

Write-Host "Copying project files..."
$items = Get-ChildItem -Path $sourceDir -Exclude "node_modules", "dist", ".git", ".github", ".agents", ".vscode", "*.log", "implementation_plan.md", "walkthrough.md", "task.md", "package-submission.ps1"

foreach ($item in $items) {
    Copy-Item -Path $item.FullName -Destination $targetSub -Recurse -Force
}

Write-Host "Compressing folder to $zipFile"
if (Test-Path $zipFile) { Remove-Item -Force $zipFile }
Compress-Archive -Path $targetBase -DestinationPath $zipFile -Force

Write-Host "Submission zip package generated successfully: $zipFile"
