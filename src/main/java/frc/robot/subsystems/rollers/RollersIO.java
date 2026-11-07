package frc.robot.subsystems.rollers;

import org.littletonrobotics.junction.AutoLog;

public interface RollersIO {

  @AutoLog
  public static class RollersIOInputs {
    double rollersVolts = 0.0;
    double statorCurrent = 0.0;
    double supplyCurrent = 0.0;
    double motorSpeed = 0.0;
  }

  public default void updateInputs(RollersIOInputs inputs) {}

  public default void runVolts(double volts) {}

  public default void stop() {}
}
