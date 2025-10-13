package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Mecanum {
    HardwareMap hardwareMap;

    private DcMotor leftFrontMotor = null;
    private DcMotor rightFrontMotor = null;
    private DcMotor leftRearMotor = null;
    private DcMotor rightRearMotor = null;

    static final double SLOW_SPEED = .3;
    static final double NORMAL_SPEED = .6;
    static final double FAST_SPEED = 1;
    static final double CLICKS_PER_INCH = 87.5;
    static final double CLICKS_PER_DEGRE = 21.94;

    private int lfPos;
    private int rfPos;
    private int lrPos;
    private int rrPos;

    public enum SPEED {
        SLOW,
        NORMAL,
        FAST
    }

    public Mecanum(HardwareMap hardwareMap) {

        this.hardwareMap = hardwareMap;
        leftFrontMotor = hardwareMap.get(DcMotor.class, "front_left");
        rightFrontMotor = hardwareMap.get(DcMotor.class, "front_right");
        leftRearMotor = hardwareMap.get(DcMotor.class, "back_left");
        rightRearMotor = hardwareMap.get(DcMotor.class, "back_right");

        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        rightFrontMotor.setDirection(DcMotor.Direction.FORWARD);
        leftRearMotor.setDirection(DcMotor.Direction.REVERSE);
        rightRearMotor.setDirection(DcMotor.Direction.FORWARD);

        // Set the drive motor run modes:
        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftRearMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRearMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Mecanum drive is controlled with three axes: drive (front-and-back),
        // strafe (left-and-right), and twist (rotating the whole chassis).
        ;
    }

    public void driveMecanum(double drive, double strafe, double twist, SPEED speed) {
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftRearMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightRearMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        double speedMultiplier;

        switch (speed) {
            case SLOW:
                speedMultiplier = SLOW_SPEED;
                break;
            case FAST:
                speedMultiplier = FAST_SPEED;
                break;
            default:
                speedMultiplier = NORMAL_SPEED;
        }

        // Calculate wheel powers.
        double leftFrontPower    =  (drive +strafe +twist) * speedMultiplier;
        double rightFrontPower   =  (drive -strafe -twist) * speedMultiplier;
        double leftBackPower     =  (drive -strafe +twist) * speedMultiplier;
        double rightBackPower    =  (drive +strafe -twist) * speedMultiplier;

        // Normalize wheel powers to be less than 1.0
        double max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        if (max > 1.0) {
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }

        // Send powers to the wheels.
        leftFrontMotor.setPower(leftFrontPower);
        rightFrontMotor.setPower(rightFrontPower);
        leftRearMotor.setPower(leftBackPower);
        rightRearMotor.setPower(rightBackPower);
    }

    public void moveForward(int howMuch, double speed) {
        // howMuch is in inches. A negative howMuch moves backward.

        // fetch motor positions
        lfPos = leftFrontMotor.getCurrentPosition();
        rfPos = rightFrontMotor.getCurrentPosition();
        lrPos = leftRearMotor.getCurrentPosition();
        rrPos = rightRearMotor.getCurrentPosition();

        // calculate new targets
        lfPos += howMuch * CLICKS_PER_INCH;
        rfPos += howMuch * CLICKS_PER_INCH;
        lrPos += howMuch * CLICKS_PER_INCH;
        rrPos += howMuch * CLICKS_PER_INCH;

        // move robot to new position
        leftFrontMotor.setTargetPosition(lfPos);
        rightFrontMotor.setTargetPosition(rfPos);
        leftRearMotor.setTargetPosition(lrPos);
        rightRearMotor.setTargetPosition(rrPos);

        leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFrontMotor.setPower(speed);
        rightFrontMotor.setPower(speed);
        leftRearMotor.setPower(speed);
        rightRearMotor.setPower(speed);

        // wait for move to complete
        while (leftFrontMotor.isBusy() && rightFrontMotor.isBusy() &&
                leftRearMotor.isBusy() && rightRearMotor.isBusy()) {

            // Display it for the driver.
            //telemetry.addLine("Move Foward");
            //telemetry.addData("Target", "%7d :%7d", lfPos, rfPos, lrPos, rrPos);
            //telemetry.addData("Actual", "%7d :%7d", leftFrontMotor.getCurrentPosition(),
            //    rightFrontMotor.getCurrentPosition(), leftRearMotor.getCurrentPosition(),
            //      rightRearMotor.getCurrentPosition());
            //telemetry.update();
        }
        // Stop all motion;
        leftFrontMotor.setPower(0);
        rightFrontMotor.setPower(0);
        leftRearMotor.setPower(0);
        rightRearMotor.setPower(0);
    }
    public void moveRight(int howMuch, double speed) {
        // howMuch is in inches. A negative howMuch moves backward.

        // fetch motor positions
        lfPos = leftFrontMotor.getCurrentPosition();
        rfPos = rightFrontMotor.getCurrentPosition();
        lrPos = leftRearMotor.getCurrentPosition();
        rrPos = rightRearMotor.getCurrentPosition();

        // calculate new targets
        lfPos += howMuch * CLICKS_PER_INCH;
        rfPos -= howMuch * CLICKS_PER_INCH;
        lrPos -= howMuch * CLICKS_PER_INCH;
        rrPos += howMuch * CLICKS_PER_INCH;

        // move robot to new position
        leftFrontMotor.setTargetPosition(lfPos);
        rightFrontMotor.setTargetPosition(rfPos);
        leftRearMotor.setTargetPosition(lrPos);
        rightRearMotor.setTargetPosition(rrPos);

        leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFrontMotor.setPower(speed);
        rightFrontMotor.setPower(speed);
        leftRearMotor.setPower(speed);
        rightRearMotor.setPower(speed);

        // wait for move to complete
        while (leftFrontMotor.isBusy() && rightFrontMotor.isBusy() &&
                leftRearMotor.isBusy() && rightRearMotor.isBusy()) {

            // Display it for the driver.
            //telemetry.addLine("Strafe Right");
            //telemetry.addData("Target", "%7d :%7d", lfPos, rfPos, lrPos, rrPos);
            //telemetry.addData("Actual", "%7d :%7d", leftFrontMotor.getCurrentPosition(),
              //      rightFrontMotor.getCurrentPosition(), leftRearMotor.getCurrentPosition(),
                //    rightRearMotor.getCurrentPosition());
            //telemetry.update();
        }

        // Stop all motion;
        leftFrontMotor.setPower(0);
        rightFrontMotor.setPower(0);
        leftRearMotor.setPower(0);
        rightRearMotor.setPower(0);

    }
    public void turnLeft(int howMuch, double speed) {
        // howMuch is in inches. A negative howMuch moves backward.

        // fetch motor positions
        lfPos = leftFrontMotor.getCurrentPosition();
        rfPos = rightFrontMotor.getCurrentPosition();
        lrPos = leftRearMotor.getCurrentPosition();
        rrPos = rightRearMotor.getCurrentPosition();

        // calculate new targets
        lfPos += -howMuch * CLICKS_PER_INCH;
        rfPos += howMuch * CLICKS_PER_INCH;
        lrPos += -howMuch * CLICKS_PER_INCH;
        rrPos += howMuch * CLICKS_PER_INCH;

        // move robot to new position
        leftFrontMotor.setTargetPosition(lfPos);
        rightFrontMotor.setTargetPosition(rfPos);
        leftRearMotor.setTargetPosition(lrPos);
        rightRearMotor.setTargetPosition(rrPos);

        leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFrontMotor.setPower(speed);
        rightFrontMotor.setPower(speed);
        leftRearMotor.setPower(speed);
        rightRearMotor.setPower(speed);

        // wait for move to complete
        while (leftFrontMotor.isBusy() && rightFrontMotor.isBusy() &&
                leftRearMotor.isBusy() && rightRearMotor.isBusy()) {

            // Display it for the driver.
            //telemetry.addLine("Move Foward");
            //telemetry.addData("Target", "%7d :%7d", lfPos, rfPos, lrPos, rrPos);
            //telemetry.addData("Actual", "%7d :%7d", leftFrontMotor.getCurrentPosition(),
            //    rightFrontMotor.getCurrentPosition(), leftRearMotor.getCurrentPosition(),
            //      rightRearMotor.getCurrentPosition());
            //telemetry.update();
        }
        // Stop all motion;
        leftFrontMotor.setPower(0);
        rightFrontMotor.setPower(0);
        leftRearMotor.setPower(0);
        rightRearMotor.setPower(0);
    }
}