import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;

/**
 * @author Dm.Petrov
 * DATE: 21.07.2022
 */
public class MedicalServiceTest {
    PatientInfo patientInfo;
    @BeforeEach
    public void patientInfoInit(){
      patientInfo = new PatientInfo("1","Ivan","Petrov",null,
                new HealthInfo(new BigDecimal("36.6"),new BloodPressure(120,80)));
    }

    @Test
    public void checkBloodPressureTest() {
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById("1")).thenReturn(patientInfo);
        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);
        medicalService.checkBloodPressure("1",new BloodPressure(140,80));
        Mockito.verify(alertService,Mockito.times(1)).
                send(String.format("Warning, patient with id: %s, need help", patientInfo.getId()));
    }
    @Test
    public void checkTemperatureTest() {
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById("1")).thenReturn(patientInfo);
        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);
        medicalService.checkTemperature("1",new BigDecimal("36.6"));
        Mockito.verify(alertService,Mockito.times(0)).
                send(String.format("Warning, patient with id: %s, need help",patientInfo.getId()));

    }
    @Test
    public void testMessage(){
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById("1")).thenReturn(patientInfo);
        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);
        medicalService.checkTemperature("1",new BigDecimal(-5));
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(alertService).send(argumentCaptor.capture());
        Assertions.assertEquals("Warning, patient with id: 1, need help",argumentCaptor.getValue());
    }
}
