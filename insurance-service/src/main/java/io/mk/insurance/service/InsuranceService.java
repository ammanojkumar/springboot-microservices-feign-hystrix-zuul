
package io.mk.insurance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import io.mk.insurance.feign.client.InsuranceClient;
import io.mk.insurance.model.InsuranceDetail;
import io.mk.insurance.model.Insurer;

/**
 * 
 * @author a.m.manojkumar@gmail.com
 * 
 */
@Service
public class InsuranceService {

	@Autowired
	private InsuranceClient insuranceClient;

	@Value("${abc.insurance.app.name}")
	String abcAppName;

	@Value("${xyz.insurance.app.name}")
	String xyzAppName;

	@HystrixCommand(fallbackMethod = "getDataFallBack")
	public InsuranceDetail getInsuranceDetail(String brand, String model) {
		InsuranceDetail insuranceDetail = new InsuranceDetail();

		Insurer insurerAbc = insuranceClient.getResponse(abcAppName, brand, model, "h1");
		Insurer insurerXyz = insuranceClient.getResponse(xyzAppName, brand, model, "h1");

		insuranceDetail.getInsurers().add(insurerAbc);
		insuranceDetail.getInsurers().add(insurerXyz);
		return insuranceDetail;
	}

	public InsuranceDetail getDataFallBack(String brand, String model) {
		return new InsuranceDetail();
	}
}
