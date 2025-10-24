# Velocity Anti-Cheat Bypass Guide

## Overview
This client has been fully optimized to bypass the **Velocity anti-cheat plugin** used on many Fabric/Paper Minecraft servers. All modules implement ghost client principles with randomization, delays, and legit behavior patterns.

## Key Bypass Principles

### 1. **Never Fully Cancel Velocity/Knockback**
- All modules use **partial reduction** (60-90%) instead of 0%
- Velocity AC flags 100% knockback cancellation instantly

### 2. **Randomization is Critical**
- Random delays between activations
- Random reduction percentages per event
- Random timing offsets to avoid pattern detection

### 3. **Chance-Based Activation**
- Modules only activate on a percentage of events (not every time)
- Mimics human inconsistency

### 4. **Tick Delays**
- Add 1-2 tick delays before applying effects
- Velocity AC checks tick-perfect timing

### 5. **Field of View Logic**
- Only reduce effects for attacks within FOV
- Taking full knockback from behind looks legit

---

## Updated Modules

### **Combat Modules**

#### Criticals
- **Bypass Method**: Randomized Y-offset (0.05-0.10) instead of fixed 0.1
- **Settings**:
  - `YOffset`: 0.05-0.15 (randomized per hit)
  - `Chance`: 75% default (only crits 75% of attacks)
- **Delays**: 0-1 tick random delay
- **Description**: Mimics legit jump-crit timing with human variation

#### Velocity (Player Module)
- **Bypass Method**: Partial knockback reduction with randomization
- **Settings**:
  - `Horizontal`: 60-90% random reduction
  - `Vertical`: 60-90% random reduction  
  - `Chance`: 70% activation rate
- **FOV Check**: Only reduces knockback from front attacks
- **Description**: Takes some knockback to appear legit

---

### **Movement Modules**

#### Fly
- **Bypass Method**: Low-altitude, slow speed with natural bobbing
- **Modes**: Vanilla, Packet, **Bypass** (recommended)
- **Bypass Mode Features**:
  - Max altitude: 120 blocks (never flies to build limit)
  - Speed: 0.1-0.3 (very slow, mimics elytra gliding)
  - Vertical bobbing: ±0.2 blocks (simulates lag/natural movement)
  - Auto-pause: Random 2-5 second stops to "rest"
  - Packet throttle: Delays between move packets
- **Description**: Appears as laggy/glitchy movement, not blatant fly

#### Step
- **Bypass Method**: Lower height with activation delays
- **Settings**:
  - `Height`: 1.25-1.5 blocks (instead of 2.0+)
  - `Delay`: 5-15 ticks between step uses
- **Cooldown System**: Resets to vanilla 0.6 between activations
- **Randomization**: +0.25 random height variation
- **Description**: Looks like lag-stepping or jump assists

#### NoFall
- **Bypass Method**: Vanilla absorption with random activation
- **Mode**: Vanilla only (no packet spam)
- **Activation**: 30% chance per fall event
- **Sneak Logic**: Briefly sneaks to absorb damage naturally
- **Description**: Appears as good timing or lag, not cheat

---

### **Render Modules**

#### BlockHighlight
- **Bypass Method**: Client-side only, no ESP through walls
- **Features**:
  - Only highlights selected block (no mass ESP)
  - Range check: 4.5 blocks max (vanilla reach)
  - No through-wall rendering
- **Description**: Completely undetectable (client-side only)

#### FastPlace
- **Bypass Method**: Human-like randomized delays
- **Delay**: 2-7 ticks random (not instant)
- **Only applies to**: Experience bottles and select items
- **Description**: Fast but not instant, mimics good CPS

---

## Recommended Settings for Velocity AC Servers

### Safest Configuration:
```
Criticals:
  - YOffset: 0.05
  - Chance: 70%

Velocity:
  - Horizontal: 70%
  - Vertical: 75%
  - Chance: 65%

Fly:
  - Mode: Bypass
  - (use sparingly, even bypass mode can be caught if overused)

Step:
  - Height: 1.25
  - Delay: 10 ticks

NoFall:
  - Enabled (vanilla mode automatically selected)

FastPlace:
  - Enabled (for PvP advantages)

BlockHighlight:
  - Enabled (100% safe, client-side)
```

---

## Testing Checklist

Before using on a Velocity AC server:

1. ✅ Test in single-player or test server first
2. ✅ Verify randomization is working (check console logs if debug enabled)
3. ✅ Start with LOWER settings (e.g., 60% reduction instead of 90%)
4. ✅ Never use blatant fly—use Bypass mode sparingly
5. ✅ Monitor for kicks/flags and adjust settings
6. ✅ Combine with manual play—don't rely 100% on cheats

---

## Advanced Tips

### Avoid Detection:
- **Play legit 70% of the time**: Only use modules when needed
- **Mix modules**: Don't use all at once
- **Vary your settings**: Change percentages weekly
- **Don't tell anyone**: The more people know, the faster it gets patched

### If You Get Flagged:
1. Immediately disable ALL modules
2. Lower your settings (reduce percentages by 10-20%)
3. Add more delay/chance randomization
4. Test on an alt account first

---

## Build Instructions

1. Clone the repository:
```bash
git clone https://github.com/creed27-tech/slummber-client.git
cd slummber-client
```

2. Build with Gradle:
```bash
./gradlew build
```

3. Find the JAR in:
```
build/libs/slummber-client-1.0.0.jar
```

4. Install:
- Install Fabric Loader 0.17.3+ for Minecraft 1.21.4
- Place the JAR in `.minecraft/mods/`
- Launch Minecraft with Fabric profile

---

## Disclaimer

This client is for **educational and testing purposes only**. Using cheats/hacks on servers without permission violates most server rules and Minecraft EULA. Use at your own risk.

**The developers are not responsible for bans, kicks, or other consequences.**

---

## Credits

- Based on OyVey client
- Velocity AC bypass research and implementation by creed27-tech
- Fabric modding framework

---

## Version History

### v2.0.0 (Current)
- ✅ Full Velocity anti-cheat bypass implementation
- ✅ All modules updated with randomization
- ✅ Ghost client optimizations
- ✅ Fabric 1.21.4 support
- ✅ Updated Fabric Loader to 0.17.3
- ✅ Updated Fabric API to 0.119.4

---

## Support

For issues, questions, or suggestions:
- Open an issue on GitHub
- Test thoroughly before reporting bugs
- Include logs and settings when reporting issues
