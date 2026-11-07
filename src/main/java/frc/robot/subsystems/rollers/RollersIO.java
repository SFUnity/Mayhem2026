package frc.robot.subsystems.rollers;

import org.littletonrobotics.junction.AutoLog;

public interface RollersIO {

  @AutoLog
  public static class RollersIOInputs {
    double rollerSpeed = 0.0;
    double rollerSupplyCurrent = 0.0;
    double rollerStatorCurrent = 0.0;
    double rollersVolts = 0.0;
    double rollersStatorCurrent = 0.0;
    double rollersSupplyCurrent = 0.0;
    double rollerSpeed = 0.0;
  }

  public default void updateInputs(RollersIOInputs inputs) {}

  public default void runVolts(double volts) {}

  public default void stop() {}
}
