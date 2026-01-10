package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class LauncherWheel {
    HardwareMap hardwareMap;

    private DcMotor launcherMotor = null;

    public LauncherWheel(HardwareMap hardwareMap) {

        this.hardwareMap = hardwareMap;
        launcherMotor = hardwareMap.get(DcMotor.class, "launcher_motor1");
        launcherMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }
    public void IntakeOn(){
        launcherMotor.setPower(0.7);

    }
    public void IntakeOff(){
        launcherMotor.setPower(0);

    }
    public void IntakeSpit(){
        launcherMotor.setPower(-0.3);

    }
    public void setIntakeSpeed (float speed){
        launcherMotor.setPower(speed);

    }
}
