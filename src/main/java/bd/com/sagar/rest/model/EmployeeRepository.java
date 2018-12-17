package bd.com.sagar.rest.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import bd.com.sagar.rest.core.bean.EmployeeBean;

@Component
public class EmployeeRepository {

	@Value("${app.fileDir}")
	private String fileDirectory;

	@Value("${app.fileName}")
	private String fileName;

	private Map<Long, EmployeeBean> dataMap;

	private long PREVIOUS_RELOAD_TIME;
	private long RELOAD_INTERVAL = 60 * 1000;

	private ObjectMapper objectMapper = new ObjectMapper();

	@PostConstruct
	protected void init() {
		File fileDir = new File(fileDirectory);
		if (!fileDir.isDirectory()) {
			fileDir.mkdirs();
		}
		fileDir = new File(fileDirectory + File.separator + fileName);
		if (!fileDir.exists() && !fileDir.isFile()) {
			try {
				fileDir.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		dataMap = new ConcurrentHashMap<>();
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}

	public List<EmployeeBean> getEmployeeList() {
		checkForReload();
		return new ArrayList<EmployeeBean>(dataMap.values());
	}

	public EmployeeBean getEmployee(Long id) {
		checkForReload();
		return dataMap.get(id);
	}

	public boolean isEmployeeExists(Long id) {
		return null != dataMap.get(id);
	}

	public synchronized void checkForReload() {
		if (System.currentTimeMillis() - PREVIOUS_RELOAD_TIME > RELOAD_INTERVAL) {
			reloadEmployeeList();
			PREVIOUS_RELOAD_TIME = System.currentTimeMillis();
		}
	}

	public synchronized boolean bulkInsertEmployeeRecord(List<EmployeeBean> employeeBeans) {
		try {
			ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
			writer.writeValue(new File((fileDirectory + File.separator + fileName)), employeeBeans);
			reloadEmployeeList();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public void reloadEmployeeList() {
		List<EmployeeBean> employeeBeans = new ArrayList<>();

		try (InputStream input = new FileInputStream(fileDirectory + File.separator + fileName)) {
			employeeBeans = objectMapper.readValue(input, new TypeReference<List<EmployeeBean>>() {
			});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		dataMap.clear();
		for (EmployeeBean employeeBean : employeeBeans) {
			dataMap.put(employeeBean.getId(), employeeBean);
		}
	}
}
