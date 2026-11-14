package frc.robot.subsystems.pivot;

import org.littletonrobotics.junction.AutoLog;

public interface PivotIO {

  @AutoLog
  public static class PivotIOInputs {
    public double pivotVolts = 0.0;
    public double pivotSupplyCurrent = 0.0;
    public double pivotStatorCurrent = 0.0;
    public double pivotSpeed = 0.0;
    public double pivotDegrees = 0.0;
  }

  public default void updateInputs(PivotIOInputs inputs) {}

  public default void requestAngle(double volts) {}

  public default void setAngle(double degrees) {}

  public default void resetAngle() {}
}
 