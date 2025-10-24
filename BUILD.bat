@echo off
echo =============================================
echo  SLUMMBER CLIENT - VELOCITY AC BYPASS BUILD
echo =============================================
echo.
echo Checking for Gradle...
gradle --version > nul 2>&1
if errorlevel 1 (
    echo ERROR: Gradle not found in PATH!
    echo.
    echo Please add Gradle 9.1.0 to your PATH:
    echo set PATH=C:\Users\legan\OneDrive\Desktop\newFOLDER\gradle-9.1.0-all\gradle-9.1.0\bin;%%PATH%%
    echo.
    pause
    exit /b 1
)

echo Gradle found! Building client...
echo.

echo Cleaning previous builds...
gradle clean

echo Building slummber-client...
gradle build

if errorlevel 1 (
    echo.
    echo BUILD FAILED!
    echo Check the error messages above.
    pause
    exit /b 1
)

echo.
echo =============================================
echo  BUILD SUCCESSFUL!
echo =============================================
echo.
echo The mod JAR is ready at:
echo build\libs\oyvey-1.0-SNAPSHOT.jar
echo.
echo To install:
echo 1. Install Fabric Loader 0.16.9+ for Minecraft 1.21.4
echo 2. Copy the JAR to your .minecraft\mods\ folder
echo 3. Launch Minecraft with Fabric profile
echo.
echo All Velocity AC bypass modules are included:
echo - Criticals (randomized, 75%% chance)
echo - Velocity (60-90%% knockback reduction)
echo - NoFall (vanilla mode, 30%% chance)
echo - FastPlace (2-7 tick delays)
echo - Fly (bypass mode - low altitude, slow speed)
echo - Step (1.25-1.5 blocks, delays)
echo - BlockHighlight (client-side only, safe)
echo.
echo Happy hacking! Use responsibly.
echo.
pause
