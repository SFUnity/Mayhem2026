package frc.robot.subsystems.rollers;

import com.ctre.phoenix6.hardware.TalonFX;

public class RollersIOTalonFX implements RollersIO{
    private final TalonFX rollerTalonFX = new TalonFX(RollersConstants.rollerID);
}
