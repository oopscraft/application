package net.oopscraft.application.core.monitor;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.junit.Test;

public class SigarTest {

	@Test
	public void test() throws Exception {

        Sigar sigar = new Sigar(); //1. sigar객체 생성

        CpuPerc cpu = sigar.getCpuPerc(); //2. 전체 cpu에 대한 사용량
        CpuPerc[] cpus = sigar.getCpuPercList(); //3. 각 cpu에 대한 사용량

        //4. cpu사용량 출력
        System.out.println("Total cpu----");
        cpu_output(cpu);
      
        for(int i=0; i < cpus.length; i++) {
             System.out.println("cpu"+i+"----");
             cpu_output(cpus[i]);
        }
		
	}

    public static void cpu_output(CpuPerc cpu) {
        System.out.println("User Time\t :"+CpuPerc.format(cpu.getUser()));
        System.out.println("Sys Time\t :"+CpuPerc.format(cpu.getSys()));
        System.out.println("Idle Time\t :"+CpuPerc.format(cpu.getSys()));        
   }
}
