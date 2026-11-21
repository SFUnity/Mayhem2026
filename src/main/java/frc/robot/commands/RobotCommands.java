package frc.robot.commands;

import static edu.wpi.first.wpilibj2.command.Commands.runOnce;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.pivot.Pivot;
import frc.robot.subsystems.pivot.Pivot.PivotState;
import frc.robot.subsystems.rollers.Rollers;
import frc.robot.subsystems.rollers.Rollers.RollersState;

public class RobotCommands {
  public static Command intake(Rollers rollers) {
    return runOnce(() -> rollers.setState(RollersState.INTAKE), rollers);
  }

  public static Command stopIntake(Rollers rollers) {
    return runOnce(() -> rollers.setState(RollersState.STOP), rollers);
  }

  public static Command outtake(Rollers rollers) {
    return runOnce(() -> rollers.setState(RollersState.OUTTAKE), rollers);
  }

  public static Command pivotBackwardL1(Pivot pivot) {
    return runOnce(() -> pivot.setState(PivotState.B_L1), pivot);
  }

  public static Command pivotBackwardL2(Pivot pivot) {
    return runOnce(() -> pivot.setState(PivotState.B_L2), pivot);
  }

  public static Command pivotForwardL1(Pivot pivot) {
    return runOnce(() -> pivot.setState(PivotState.F_L1), pivot);
  }

  public static Command pivotForwardL2(Pivot pivot) {
    return runOnce(() -> pivot.setState(PivotState.F_L2), pivot);
  }

  public static Command pivotForwardDown(Pivot pivot) {
    return runOnce(() -> pivot.setState(PivotState.F_DOWN), pivot);
  }
}
