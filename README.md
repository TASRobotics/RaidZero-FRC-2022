# RaidZero-FRC-2022
Robot code for the 2022 FRC competition

### kinetic shooting math: 
- calculate linear velocity of ball
- calculate tof by using linear vel of ball, angle, initial height, and desired height
- calculate chassis rate of change
- using chassis rate of change + tof, calculate how far off the ball will land
- use turret & hood to account for offset