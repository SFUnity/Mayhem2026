// Copyright (c) 2021-2026 Littleton Robotics
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by a BSD
// license that can be found in the LICENSE file
// at the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.Seconds;
import static frc.robot.subsystems.vision.VisionConstants.*;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.DriveCommands;
import frc.robot.generated.TunerConstants;
import frc.robot.lib.BLine.FollowPath;
import frc.robot.lib.BLine.Path;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.DriveConstants;
import frc.robot.subsystems.drive.GyroIO;
import frc.robot.subsystems.drive.GyroIOPigeon2;
import frc.robot.subsystems.drive.ModuleIO;
import frc.robot.subsystems.drive.ModuleIOSim;
import frc.robot.subsystems.drive.ModuleIOTalonFX;
import frc.robot.subsystems.vision.Vision;
import frc.robot.subsystems.vision.VisionIO;
import frc.robot.subsystems.vision.VisionIOLimelight;
import frc.robot.util.FuelSim;
import frc.robot.util.PoseManager;
import frc.robot.util.ShiftHelpers;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedNetworkBoolean;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // Subsystems
  private final Drive drive;
  private final Vision vision;

  // Non-subsystems
  // private final Autos autos;
  private final PoseManager poseManager = new PoseManager();
  public final FuelSim fuelSim = new FuelSim("FuelSim");

  // Controller
  private final CommandXboxController controller = new CommandXboxController(0);
  private final Alert driverDisconnected =
      new Alert("Driver controller disconnected (port 0).", AlertType.kWarning);

  // Alerts
  private static final double canErrorTimeThreshold = 0.5; // Seconds to disable alert
  private static final double lowBatteryVoltage = 12.5;
  private static final double extraLowBatteryVoltage = 11.5;
  private static final double lowBatteryDisabledTime = 1.5;

  private final Timer disabledTimer = new Timer();
  private final Timer canInitialErrorTimer = new Timer();
  private final Timer canErrorTimer = new Timer();

  private final Alert canErrorAlert = // YAY always fun
      new Alert("CAN errors detected, robot may not be controllable.", AlertType.kError);
  private final Alert lowBatteryAlert =
      new Alert(
          "Battery voltage is very low, consider turning off the robot or replacing the battery.",
          AlertType.kWarning);

  // Callback for fuel sim intake
  private double fuelCount = 0;

  // Demo button for safety
  private final LoggedNetworkBoolean demoMode =
      new LoggedNetworkBoolean("Demo Mode Speed Limit", true);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  @SuppressWarnings("resource")
  public RobotContainer() {
    // Reset alert timers
    canInitialErrorTimer.restart();
    canErrorTimer.restart();
    disabledTimer.restart();

    // Alerts for constants
    if (Constants.tuningMode) {
      new Alert("Tuning mode enabled", AlertType.kInfo).set(true);
    }

    switch (Constants.currentMode) {
      case REAL:
        // Real robot, instantiate hardware IO implementations
        // drive =
        //     new Drive(
        //         new GyroIO() {},
        //         new ModuleIO() {},
        //         new ModuleIO() {},
        //         new ModuleIO() {},
        //         new ModuleIO() {},
        //         poseManager);

        // * working ones below this line
        drive =
            new Drive(
                new GyroIOPigeon2(),
                new ModuleIOTalonFX(TunerConstants.FrontLeft),
                new ModuleIOTalonFX(TunerConstants.FrontRight),
                new ModuleIOTalonFX(TunerConstants.BackLeft),
                new ModuleIOTalonFX(TunerConstants.BackRight),
                poseManager);

        // vision = new Vision(poseManager, new VisionIO() {});
        vision =
            new Vision(
                poseManager,
                new VisionIOLimelight(portFore),
                new VisionIOLimelight(portAft),
                new VisionIOLimelight(starboardAft));
        break;

      case SIM:
        // Sim robot, instantiate physics sim IO implementations
        drive =
            new Drive(
                new GyroIO() {},
                new ModuleIOSim(TunerConstants.FrontLeft),
                new ModuleIOSim(TunerConstants.FrontRight),
                new ModuleIOSim(TunerConstants.BackLeft),
                new ModuleIOSim(TunerConstants.BackRight),
                poseManager);
        vision = new Vision(poseManager, new VisionIO() {});

        fuelSim.spawnStartingFuel(); // spawns fuel in the depots and neutral zone
        // Register a robot for collision with fuel
        fuelSim
            .start(); // enables the simulation to run (updateSim must still be called periodically)
        fuelSim.registerRobot(
            Units.inchesToMeters(DriveConstants.widthInches)
                + 2 * DriveConstants.bumperWidth, // from left to right in meters
            Units.inchesToMeters(DriveConstants.lengthInches)
                + 2 * DriveConstants.bumperWidth, // from front to back in meters
            DriveConstants.bumperHeight, // from floor to top of bumpers in meters
            () -> poseManager.getPose(), // Supplier<Pose2d> of robot pose
            () ->
                drive.getFieldSpeeds()); // Supplier<ChassisSpeeds> of field-centric chassis speeds

        // Register an intake to remove fuel from the field as a rectangular bounding box
        double minX = Units.inchesToMeters(17.049462);
        double maxX = Units.inchesToMeters(25.548604);
        double minY = Units.inchesToMeters(-12.625);
        double maxY = Units.inchesToMeters(12.625);
        break;

      default:
        // Replayed robot, disable IO implementations
        drive =
            new Drive(
                new GyroIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                poseManager);
        vision = new Vision(poseManager, new VisionIO() {});
        break;
    }

    // autos =
    //     new Autos(
    //         drive,
    //         poseManager,
    //         intakeRollers,
    //         intakePivot,
    //         shooter,
    //         kicker,
    //         spindexer,
    //         climb,
    //         hood);

    // For tuning visualizations
    // Logger.recordOutput("ZeroedPose2d", new Pose2d());
    Logger.recordOutput("ZeroedPose3d", new Pose3d());

    // Configure the button bindings
    configureButtonBindings();

    FollowPath.Builder pathBuilder =
        new FollowPath.Builder(
                drive,
                poseManager::getPose,
                poseManager::getChassisSpeeds,
                drive::runVelocity,
                new PIDController(2.0, 0.0, 0.0),
                new PIDController(1.0, 0.0, 0.0),
                new PIDController(0.2, 0.0, 0.0))
            .withDefaultShouldFlip()
            .withTRatioBasedTranslationHandoffs(true);

    Path firstStraight = new Path("first-straight");

    Command firstAuto = pathBuilder.withPoseReset(poseManager::setPose).build(firstStraight);

    // Builder options persist. Clear this before building any later path command.
    pathBuilder.withPoseReset(ignored -> {});
  }

  public void checkAlerts() {
    // Check controllers
    boolean driverConnected =
        controller.isConnected()
            && DriverStation.getJoystickIsXbox(
                controller.getHID().getPort()); // Should be an XBox controller
    driverDisconnected.set(!driverConnected);
    Logger.recordOutput("Controls/driverConnected", driverConnected);

    // Check CAN status
    var canStatus = RobotController.getCANStatus();
    if (canStatus.transmitErrorCount > 0 || canStatus.receiveErrorCount > 0) {
      canErrorTimer.restart();
    }
    canErrorAlert.set(
        !canErrorTimer.hasElapsed(canErrorTimeThreshold)
            && canInitialErrorTimer.hasElapsed(canErrorTimeThreshold));

    // Low battery alert
    if (DriverStation.isEnabled()) {
      disabledTimer.reset();
    }
    if (disabledTimer.hasElapsed(lowBatteryDisabledTime)) {
      double voltage = RobotController.getBatteryVoltage();
      if (voltage <= extraLowBatteryVoltage) {
        lowBatteryAlert.set(true);
      } else if (voltage <= lowBatteryVoltage) {
        lowBatteryAlert.set(true);
      }
    }
  }

  /**
   * Green light when the hub is active, white light when it's inactive. Three seconds before the
   * end of a shift, the light will blink white and green. During transition periods yellow light.
   */
  public void dashboardSetup() {
    Color hubOff = new Color(255, 255, 255);
    Color hubOn = new Color(0, 255, 0);
    Color transitionPeriod = new Color(255, 255, 0);
    Color shiftColor = hubOff;
    double shiftTime =
        ShiftHelpers.timeRemainingInCurrentShift().orElse(Seconds.of(0.0)).in(Seconds);

    if (ShiftHelpers.getCurrentShift().orElse(null) == ShiftHelpers.Shift.TRANSITION
        || ShiftHelpers.getCurrentShift().orElse(null) == ShiftHelpers.Shift.ENDGAME) {
      shiftColor = transitionPeriod;
    } else if (shiftTime < 3.0 && shiftTime > 0.0) {
      shiftColor = (System.currentTimeMillis() % 1000 < 500) ? hubOff : hubOn;
    } else if (ShiftHelpers.isActive() == true) {
      shiftColor = hubOn;
    } else {
      shiftColor = hubOff;
    }
    Logger.recordOutput("Controls/Shift Color", shiftColor);
    Logger.recordOutput(
        "Controls/Shift Time",
        Math.round(
                ShiftHelpers.timeRemainingInCurrentShift().orElse(Seconds.of(0.0)).in(Seconds)
                    * 10.0)
            / 10.0);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // Default command, normal field-relative drive
    // drive.setDefaultCommand(
    //     DriveCommands.joystickDrive(
    //         drive,
    //         () -> -controller.getLeftY(),
    //         () -> -controller.getLeftX(),
    //         () -> -controller.getRightX(),
    //         poseManager,
    //         () -> intakePivot.intakeDown()));
    drive.setDefaultCommand(
        DriveCommands.toggleLimitSpeed(
            drive,
            () -> -controller.getLeftY(),
            () -> -controller.getLeftX(),
            () -> -controller.getRightX(),
            poseManager,
            demoMode::get));

    // Switch to X pattern when X button is pressed
    controller.x().onTrue(Commands.runOnce(drive::stopWithX, drive));

    // Reset gyro to 0° when start or back button is pressed
    controller
        .start()
        .or(controller.back())
        .onTrue(
            Commands.runOnce(
                    () ->
                        poseManager.setPose(
                            new Pose2d(poseManager.getPose().getTranslation(), Rotation2d.kZero)),
                    drive)
                .ignoringDisable(true));

    // Controller rumble when 5 seconds are left in the current shift
    new Trigger(
            () ->
                ShiftHelpers.timeRemainingInCurrentShift().orElse(Seconds.of(10.0)).in(Seconds)
                    < 5.0)
        .and(() -> DriverStation.isTeleopEnabled())
        .onTrue(
            Commands.runEnd(
                    () -> controller.setRumble(GenericHID.RumbleType.kBothRumble, 0.5),
                    () -> controller.setRumble(GenericHID.RumbleType.kBothRumble, 0.0))
                .withTimeout(0.50));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  // public Command getAutonomousCommand() {
  //   return autos.getAutonomousCommand();
  // }

}
