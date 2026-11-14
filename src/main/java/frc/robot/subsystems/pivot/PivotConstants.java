package frc.robot.subsystems.pivot;

import edu.wpi.first.math.util.Units;

public class PivotConstants {
  public static final int pivotID = 1;
  public static final double pivotVolts = 5.0;
  public static final double pivotStatorCurrentThreshold = 20.0;
  public static final double minAngleRads = Units.degreesToRadians(10);
  public static final double maxAngleRads = Units.degreesToRadians(135);
  public static final double armLengthMeters = Units.inchesToMeters(15);
}
