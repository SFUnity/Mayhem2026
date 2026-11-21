package frc.robot;

import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
import choreo.trajectory.SwerveSample;
import choreo.trajectory.Trajectory;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
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

    chooser.addRoutine("LowerStartGrabCubeAndBalance", this::LowerStartGrabCubeAndBalance);
    chooser.addRoutine("LowerDoubleDrabCubeBalance", this::LowerDoubleGrabCubeBalance);
    chooser.addRoutine("UpperStartGrabCubeBalance", this::UpperStartGrabCubeBalance);
    chooser.addRoutine("PickupCubeOverBeam", this::PickupCubeOverBeam);
    chooser.addRoutine("UpperExitBalance", this::UpperExitBalance);
    chooser.addRoutine("LowerExitBalance", this::LowerExitBalance);
    chooser.addRoutine("UpperDoubleGrab", this::UpperDoubleGrab);
    chooser.addRoutine("LowerBalance", this::LowerBalance);
    chooser.addRoutine("UpperBalance", this::UpperBalance);
    // chooser.addRoutine("UpperGrabOnceandBalanceWithCommands",
    // this::UpperGrabOnceandBalanceWithCommands);

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
        nonChoreoChooser.addOption("TuneModuleDrive", drive.tuneModuleDrive());
        nonChoreoChooser.addOption("TuneModuleTurn", drive.tuneModuleTurn());
        // Set up SysId routines
        nonChoreoChooser.addOption(
            "Drive Wheel Radius Characterization", drive.wheelRadiusCharacterization());
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

  public AutoRoutine LowerStartGrabCubeAndBalance() {
    AutoRoutine routine = factory.newRoutine("LowerStartGrabCubeAndBalance");
    AutoTrajectory LowerStartGrabCubeAndBalance =
        routine.trajectory("LowerStartGrabCubeAndBalance");
    routine
        .active()
        .onTrue(
            Commands.sequence(
                LowerStartGrabCubeAndBalance.resetOdometry(), LowerStartGrabCubeAndBalance.cmd()));
    return routine;
  }

  public AutoRoutine LowerDoubleGrabCubeBalance() {
    AutoRoutine routine = factory.newRoutine("LowerDoubleGrabCubeBalance");
    AutoTrajectory LowerDoubleGrabCubeBalance = routine.trajectory("LowerDoubleGrabCubeBalance");
       routine.active().onTrue(Commands.sequence(LowerDoubleGrabCubeBalance.resetOdometry(), LowerDoubleGrabCubeBalance.cmd()));
    return routine;
  }

  public AutoRoutine UpperStartGrabCubeBalance() {
    AutoRoutine routine = factory.newRoutine("UpperStartGrabCubeBalance");
    AutoTrajectory UpperStartGrabCubeBalance = routine.trajectory("UpperStartGrabCubeBalance");
    routine.active().onTrue(Commands.sequence(UpperStartGrabCubeBalance.resetOdometry(), UpperStartGrabCubeBalance.cmd()));
    return routine;
  }

  public AutoRoutine PickupCubeOverBeam() {
    AutoRoutine routine = factory.newRoutine("PickupCubeOverBeam");
    AutoTrajectory PickupCubeOverBeam = routine.trajectory("PickupCubeOverBeam");
    routine.active().onTrue(Commands.sequence(PickupCubeOverBeam.resetOdometry(), PickupCubeOverBeam.cmd()));
    return routine;
  }

  public AutoRoutine UpperExitBalance() {
    AutoRoutine routine = factory.newRoutine("UpperExitBalance");
    AutoTrajectory UpperExitBalance = routine.trajectory("UpperExitBalance");
    routine.active().onTrue(Commands.sequence(UpperExitBalance.resetOdometry(), UpperExitBalance.cmd()));
    return routine;
  }

  public AutoRoutine LowerExitBalance() {
    AutoRoutine routine = factory.newRoutine("LowerExitBalance");
    AutoTrajectory LowerExitBalance = routine.trajectory("LowerExitBalance");
    routine.active().onTrue(Commands.sequence(LowerExitBalance.resetOdometry(), LowerExitBalance.cmd()));
    return routine;
  }

  public AutoRoutine UpperDoubleGrab() {
    AutoRoutine routine = factory.newRoutine("UpperDoubleGrab");
    AutoTrajectory UpperDoubleGrab = routine.trajectory("UpperDoubleGrab");
    routine.active().onTrue(Commands.sequence(UpperDoubleGrab.resetOdometry(), UpperDoubleGrab.cmd()));
    return routine;
  }

  public AutoRoutine LowerBalance() {
    AutoRoutine routine = factory.newRoutine("LowerBalance");
    AutoTrajectory LowerBalance = routine.trajectory("LowerBalance");
    routine.active().onTrue(Commands.sequence(LowerBalance.resetOdometry(), LowerBalance.cmd()));
    return routine;
  }

  public AutoRoutine UpperBalance() {
    AutoRoutine routine = factory.newRoutine("UpperBalance");
    AutoTrajectory UpperBalance = routine.trajectory("UpperBalance");
    routine.active().onTrue(Commands.sequence(UpperBalance.resetOdometry(), UpperBalance.cmd()));
    return routine;
  }
  /* No clue how to fix this, but they are separated paths so that the robot can stop to intake,
  but I don't know how to robot works. If it can just run into blocks and intake like that, then we can use similar code
  to Claude and have it intake on checkpoints -> number checkpoints so we can use them

    public AutoRoutine UpperGrabOnceandBalanceWithCommands() {
      AutoRoutine routine = factory.newRoutine("UpperGrabOnceandBalanceWithCommands");

      AutoTrajectory UpperToDrop = routine.trajectory("UpperToDrop");
      AutoTrajectory DropToCenter = routine.trajectory("DropToCenter");
      AutoTrajectory CenterToUpperDrop = routine.trajectory("CenterToUpperDrop");
      AutoTrajectory UpperDropToUpperBalance = routine.trajectory("UpperDropToUpperBalance");

       routine
         .active()
         .onTrue(
         Commands.sequence(
           UpperToDrop.resetOdometry(), UpperToDrop.cmd(), RobotCommands.outtake(Rollers rollers), DropToCenter.cmd(),
           RobotCommands.intake(Rollers rollers), RobotCommands.stopIntake(Rollers rollers), CenterToUpperDrop.cmd(),
           RobotCommands.outtake(Rollers rollers), UpperDropToUpperBalance.cmd()));

      return routine;
    }
     */
}
