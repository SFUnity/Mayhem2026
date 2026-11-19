package frc.robot.subsystems.pivot;

import edu.wpi.first.math.util.Units;

public class PivotConstants {
  public static final int pivotID = 1;
  public static final double pivotVolts = 5.0;
  public static final double pivotStatorCurrentThreshold = 20.0;
  public static final double minAngleDegs = 10;
  public static final double maxAngleDegs = 135;
  public static final double armLengthMeters = Units.inchesToMeters(15);
  public static final double gearRatio = 45.0 * 34.0/12.0;
  public static final int motorID = 0;
  public static final double supplyCurrentLimit = 80.0;
  public static final double kP = 0.1;
  public static final double kD = 0.0;
  public static final double kV= 0.0;
  public static final double acceleration = Units.degreesToRotations(90.0);
  public static final double velocity = Units.degreesToRotations(45.0);
  public static final double F_DOWN_ANGLE = Units.degreesToRotations(0.0);
  public static final double F_L1_ANGLE = Units.degreesToRotations(3.82);
  public static final double F_L1Stack_ANGLE = Units.degreesToRotations(27.2);
  public static final double F_L2_ANGLE = Units.degreesToRotations(50.0);
  public static final double B_L2_ANGLE = Units.degreesToRotations(130.0);
  public static final double B_L1Stack_ANGLE = Units.degreesToRotations(152.2);
  public static final double B_L1_ANGLE = Units.degreesToRotations(176.2);


}
