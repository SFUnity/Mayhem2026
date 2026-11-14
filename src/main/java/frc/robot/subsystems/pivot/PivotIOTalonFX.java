package frc.robot.subsystems.pivot;

import java.io.ObjectInputFilter.Config;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;

import edu.wpi.first.math.util.Units;

public class PivotIOTalonFX implements PivotIO {
  private final TalonFX pivot;
  private final TalonFXConfiguration config;

  private final MotionMagicVoltage motionMagicVoltage = new MotionMagicVoltage(0.0).withEnableFOC(true);

  public PivotIOTalonFX() {
    pivot = new TalonFX(PivotConstants.motorID);
    config = new TalonFXConfiguration();
    config.CurrentLimits.SupplyCurrentLimit = PivotConstants.SupplyCurrentLimit;
    config.CurrentLimits.SupplyCurrentLimitEnable = true;
    config.Feedback.SensorToMechanismRatio = PivotConstants.gearRatio;

    config.Slot0.GravityType = GravityTypeValue.Arm_Cosine;
    config.Slot0.kP = PivotConstants.kP;
    config.Slot0.kD = PivotConstants.kD;
    config.Slot0.kV = PivotConstants.kV;
    config.MotionMagic.MotionMagicAcceleration = PivotConstants.acceleration;
    config.MotionMagic.MotionMagicCruiseVelocity = PivotConstants.velocity;
    pivot.getConfigurator().apply(config);
  }

  @Override
  public void updateInputs(PivotIOInputs inputs) {
    inputs.pivotVolts = pivot.getMotorVoltage().getValueAsDouble();
    inputs.pivotSupplyCurrent = pivot.getSupplyCurrent().getValueAsDouble();
    inputs.pivotStatorCurrent = pivot.getStatorCurrent().getValueAsDouble();
    inputs.pivotSpeed = pivot.getVelocity().getValueAsDouble();
    inputs.pivotDegrees = Units.rotationsToDegrees(pivot.getPosition().getValueAsDouble());
  }

  @Override
public void requestMotorAngle(double degrees) {
    motionMagicVoltage.Position = Units.degreesToRotations(degrees);
    pivot.setControl(motionMagicVoltage);
  }

  @Override
  public void setEncoderAngle(double degrees) {
    pivot.setPosition(Units.degreesToRotations(degrees));
  }

  @Override
  public void resetMotorAngle() {
    setEncoderAngle(PivotConstants.minAngleDegs);
  }
}
