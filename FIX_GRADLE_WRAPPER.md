# How to Fix Gradle Wrapper to 9.1.0

## Problem
The Gradle wrapper is not properly configured for Gradle 9.1.0, preventing the project from building.

## Solution: Use Your Local Gradle 9.1.0 to Regenerate the Wrapper

Since you have Gradle 9.1.0 installed locally, you can use it to regenerate the wrapper files properly.

### Step-by-Step Fix:

#### 1. Clone the Repository
```bash
cd C:\Users\legan\OneDrive\Desktop\newFOLDER
git clone https://github.com/creed27-tech/slummber-client.git
cd slummber-client
```

#### 2. Delete the Old Wrapper Files
```bash
# On Windows Command Prompt:
rmdir /s /q gradle\wrapper

# Or on PowerShell:
Remove-Item -Recurse -Force gradle\wrapper
```

#### 3. Regenerate Wrapper Using Your Local Gradle 9.1.0

**IMPORTANT:** Make sure Gradle 9.1.0 is in your PATH first:

```bash
# Check your Gradle version
gradle --version

# Should show:
# Gradle 9.1.0
```

If it shows a different version, you need to add Gradle 9.1.0 to your PATH:

```bash
# Windows - Add to PATH temporarily:
set PATH=C:\Users\legan\OneDrive\Desktop\newFOLDER\gradle-9.1.0-all\gradle-9.1.0\bin;%PATH%

# Or set it permanently through System Environment Variables:
# 1. Search "Environment Variables" in Windows
# 2. Edit PATH
# 3. Add: C:\Users\legan\OneDrive\Desktop\newFOLDER\gradle-9.1.0-all\gradle-9.1.0\bin
```

#### 4. Generate New Wrapper
```bash
gradle wrapper --gradle-version 9.1.0 --distribution-type all
```

This will:
- Create a new `gradle/wrapper/` folder
- Download `gradle-wrapper.jar` (binary file) for version 9.1.0
- Create `gradle-wrapper.properties` with correct URL
- Update `gradlew` and `gradlew.bat` scripts

#### 5. Verify the Wrapper
```bash
# Test the wrapper
gradlew.bat --version

# Should show:
# Gradle 9.1.0
```

#### 6. Test Building the Project
```bash
gradlew.bat clean build
```

#### 7. Commit and Push the Fixed Wrapper
```bash
git add gradle/wrapper/
git add gradlew gradlew.bat
git commit -m "Fix Gradle wrapper - regenerate for 9.1.0"
git push origin master
```

---

## Alternative: Use gradlew Directly (If Wrapper Works)

If you just want to build without fixing the wrapper on GitHub:

```bash
# Just use your local Gradle instead of the wrapper:
gradle clean build

# The output JAR will still be in:
build\libs\oyvey-1.0-SNAPSHOT.jar
```

---

## Why This Happens

The Gradle wrapper consists of:
1. **gradle-wrapper.properties** - Text file with download URL (we updated this)
2. **gradle-wrapper.jar** - Binary file that downloads and runs Gradle (THIS is the problem - it's from an older version)
3. **gradlew / gradlew.bat** - Scripts that use the wrapper

Just changing the `.properties` file isn't enough - the **JAR file itself** needs to be from Gradle 9.1.0. GitHub doesn't let us upload binary files through the web interface, so you need to regenerate it locally.

---

## Quick Build Instructions (After Fix)

```bash
# Clone the repo
git clone https://github.com/creed27-tech/slummber-client.git
cd slummber-client

# Build using the wrapper
gradlew.bat clean build

# Or use your local Gradle
gradle clean build

# Install the mod
# 1. Install Fabric Loader 0.16.9+ for Minecraft 1.21.4
# 2. Copy build\libs\oyvey-1.0-SNAPSHOT.jar to .minecraft\mods\
# 3. Launch Minecraft with Fabric profile
```

---

## Still Having Issues?

If the wrapper still doesn't work:

1. **Check Java version:**
   ```bash
   java -version
   # Should be Java 21
   ```

2. **Use your local Gradle directly:**
   ```bash
   gradle clean build
   ```

3. **Delete the `.gradle` cache:**
   ```bash
   rmdir /s /q .gradle
   rmdir /s /q build
   gradle clean build
   ```

4. **Check for errors in:**
   - `gradle.properties` (should have `loader_version=0.16.9`)
   - `build.gradle` (should compile without issues)

---

## Summary

**The fix:** Run `gradle wrapper --gradle-version 9.1.0 --distribution-type all` from inside the cloned repository using your local Gradle 9.1.0 installation. This will regenerate all wrapper files properly.

Once you've done this locally and pushed the changes, the wrapper will work for everyone who clones the repository.
