package frc.robot.subsystems.pivot;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.math.util.Units;

public class PivotIOTalonFX implements PivotIO {
  private final TalonFX pivot;
  private final TalonFXConfiguration config;

  public PivotIOTalonFX() {
    pivot = new TalonFX(PivotConstants.motorID);
    config = new TalonFXConfiguration();
    config.CurrentLimits.SupplyCurrentLimit = PivotConstants.supplyCurrentLimit;
    config.CurrentLimits.SupplyCurrentLimitEnable = true;
    config.Feedback.SensorToMechanismRatio = PivotConstants.gearRatio;
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
  public void setEncoderAngle(double degrees) {
    pivot.setPosition(Units.degreesToRotations(degrees));
  }

  @Override
  public void resetEncoderAngle() {
    setEncoderAngle(PivotConstants.minAngleDegs);
  }
}
