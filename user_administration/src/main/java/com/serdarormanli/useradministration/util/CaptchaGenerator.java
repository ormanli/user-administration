package com.serdarormanli.useradministration.util;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import com.github.cage.Cage;
import com.github.cage.YCage;
import com.serdarormanli.useradministration.exception.InvalidCaptchaException;

public class CaptchaGenerator {

	private ConcurrentHashMap<String, String> captchaList = new ConcurrentHashMap<String, String>();

	private CaptchaGenerator() {
	}

	private static class SingletonHolder {
		private static final CaptchaGenerator INSTANCE = new CaptchaGenerator();
	}

	public static CaptchaGenerator getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public synchronized HashMap<String, String> getCaptcha() throws Exception {
		Cage cage = new YCage();

		String captchaValue = RandomStringUtils.randomAlphanumeric(8);
		String id = UUID.randomUUID().toString();
		byte[] c = cage.draw(captchaValue);

		HashMap<String, String> result = new HashMap<String, String>();
		result.put("ID", id);
		result.put("VALUE", new StringBuffer("data:image/gif;base64,").append(Base64.encodeBase64String(c)).toString());

		captchaList.put(id, captchaValue);

		return result;
	}

	public synchronized boolean checkCaptcha(String id, String value) throws Exception {
		boolean result = captchaList.containsKey(id) && StringUtils.equals(captchaList.get(id), value);

		captchaList.remove(id);

		if (!result) {
			throw new InvalidCaptchaException("You entered wrong value!");
		}

		return result;
	}
}
