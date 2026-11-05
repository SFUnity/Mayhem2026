package frc.robot;

import choreo.auto.AutoFactory;
import choreo.trajectory.SwerveSample;
import choreo.trajectory.Trajectory;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.commands.DriveCommands;
import frc.robot.subsystems.drive.Drive;
import frc.robot.util.LoggedAutoChooser;
import frc.robot.util.PoseManager;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

public class Autos {
  private final Drive drive;
  private final PoseManager poseManager;

  private final AutoFactory factory;
  private final LoggedAutoChooser chooser;

  private final LoggedDashboardChooser<Command> nonChoreoChooser =
      new LoggedDashboardChooser<Command>("Non-Choreo Chooser");
  private static final boolean isChoreoAuto = true;

  public static boolean moveRight = false;
  public static boolean moveLeft = false;

  public Autos(Drive drive, PoseManager poseManager) {
    this.drive = drive;
    this.poseManager = poseManager;

    factory =
        new AutoFactory(
            poseManager::getPose,
            poseManager::setPose,
            drive::followTrajectory,
            true,
            drive,
            (Trajectory<SwerveSample> traj, Boolean bool) -> {
              //   Logger.recordOutput(
              //       "Drive/Choreo/Active Traj",
              //       (AllianceFlipUtil.shouldFlip() ? traj.flipped() : traj).getPoses());
              //   Logger.recordOutput(
              //       "Drive/Choreo/Current Traj End Pose",
              //       traj.getFinalPose(AllianceFlipUtil.shouldFlip()).get());
              //   Logger.recordOutput(
              //       "Drive/Choreo/Current Traj Start Pose",
              //       traj.getInitialPose(AllianceFlipUtil.shouldFlip()).get());
            });

    /* Set up main choreo routines */
    chooser = new LoggedAutoChooser("/ChoreoChooser");

    // not as necessary
    // chooser.addRoutine("Climb", this::climbAutoRoutine);
    // chooser.addRoutine("Outpost Climb", this::outpostClimbAutoRoutine);
    // chooser.addRoutine("Depot Feed", this::depotFeedAutoRoutine);
    // chooser.addRoutine("Upper Feed Climb", this::upperFeedClimbAutoRoutine);
    // chooser.addRoutine("Lower Feed Climb", this::LowerFeedClimbAutoRoutine);

    if (!DriverStation.isFMSAttached()) {
      // Set up test choreo routines

      // SysID & non-choreo routines
      if (!isChoreoAuto) {
        nonChoreoChooser.addOption("TuneModuleDrive", DriveCommands.tuneModuleDrive(drive));
        nonChoreoChooser.addOption("TuneModuleTurn", DriveCommands.tuneModuleTurn(drive));
        // Set up SysId routines
        nonChoreoChooser.addOption(
            "Drive Wheel Radius Characterization",
            DriveCommands.wheelRadiusCharacterization(drive, poseManager));
        nonChoreoChooser.addOption(
            "Drive Simple FF Characterization", DriveCommands.feedforwardCharacterization(drive));
        nonChoreoChooser.addOption(
            "Drive SysId (Quasistatic Forward)",
            drive.sysIdQuasistatic(SysIdRoutine.Direction.kForward));
        nonChoreoChooser.addOption(
            "Drive SysId (Quasistatic Reverse)",
            drive.sysIdQuasistatic(SysIdRoutine.Direction.kReverse));
        nonChoreoChooser.addOption(
            "Drive SysId (Dynamic Forward)", drive.sysIdDynamic(SysIdRoutine.Direction.kForward));
        nonChoreoChooser.addOption(
            "Drive SysId (Dynamic Reverse)", drive.sysIdDynamic(SysIdRoutine.Direction.kReverse));
      }
    }
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return isChoreoAuto ? chooser.selectedCommandScheduler() : nonChoreoChooser.get();
  }
}
