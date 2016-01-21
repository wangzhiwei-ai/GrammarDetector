package edu.pku.ss.nlp.toolkit;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

import org.apache.commons.io.FileUtils;

public class LibSvmModel implements Model {
	private final String trainFilePath = "res/subjunctive/trainingSet.txt";
	private final String testFilePath = "res/subjunctive/testingSet.txt";
	private final String modelFilePath = "res/subjunctive/model.txt";
	private final String splitSymbol = "\t";

	private svm_node[][] trainPoint;
	private double[] trainLables;
	private svm_node[][] testPoint;
	private double[] testLables;

	private int trainInstanceNumber = 0;
	private int testInstanceNumber = 0;
	private int attributeNumber = 0;
	private int svmType = svm_parameter.C_SVC;
	private int kernelType = svm_parameter.LINEAR;
	private int cacheSize = 100000;
	private double eps = 0.00001;
	private int C = 1;
	private svm_model model;

	public LibSvmModel(String trainFilePath, String testFilePath,
			String modelFilePath) {

		this.trainInstanceNumber = countInstances(trainFilePath);
		this.testInstanceNumber = countInstances(testFilePath);

		this.trainPoint = new svm_node[trainInstanceNumber][attributeNumber];
		this.testPoint = new svm_node[testInstanceNumber][attributeNumber];
		this.trainLables = new double[trainInstanceNumber];
		this.testLables = new double[testInstanceNumber];

		loadData(this.trainFilePath, this.trainPoint, this.trainLables);
		loadData(this.testFilePath, this.testPoint, this.testLables);
	}

	public LibSvmModel(String testInstance, boolean flag) {

		this.trainInstanceNumber = countInstances(trainFilePath);

		this.trainPoint = new svm_node[trainInstanceNumber][attributeNumber];
		this.trainLables = new double[trainInstanceNumber];
		this.testPoint = new svm_node[1][attributeNumber];
		this.testLables = new double[1];

		loadData(this.trainFilePath, this.trainPoint, this.trainLables);
		initTestData(testInstance, this.testPoint, this.testLables);
	}

	public LibSvmModel(String testInstance) {

		attributeNumber = getAttributeNumber(testInstance);
		this.testPoint = new svm_node[1][attributeNumber];
		this.testLables = new double[1];

		initTestData(testInstance, this.testPoint, this.testLables);
	}

	public int getAttributeNumber(String instance) {
		String[] temp = instance.split(splitSymbol);
		return temp.length - 1;
	}

	public void initTestData(String testInstance, svm_node[][] point,
			double[] lables) {
		String[] temp = testInstance.split(splitSymbol);

		lables[0] = Double.parseDouble(temp[0]);
		for (int j = 1; j < temp.length; j++) {
			point[0][j - 1] = new svm_node();
			point[0][j - 1].index = j - 1;
			point[0][j - 1].value = Double.parseDouble(temp[j]);
		}
	}

	public int countInstances(String readPath) {
		try {
			List<String> lines = FileUtils.readLines(new File(readPath));
			String[] temp = lines.get(0).split(splitSymbol);
			this.attributeNumber = temp.length - 1;
			return lines.size();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void loadData(String readPath, svm_node[][] point, double[] lables) {
		try {
			List<String> lines = FileUtils.readLines(new File(readPath));

			for (int i = 0; i < lines.size(); i++) {
				String[] temp = lines.get(i).split(splitSymbol);

				// System.out.println("line ----"+(i+1));
				lables[i] = Double.parseDouble(temp[0]);
				for (int j = 1; j < temp.length; j++) {
					point[i][j - 1] = new svm_node();
					point[i][j - 1].index = j - 1;
					point[i][j - 1].value = Double.parseDouble(temp[j]);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public svm_problem setProblem() {
		svm_problem problem = new svm_problem();
		problem.l = this.trainInstanceNumber;
		problem.x = this.trainPoint;
		problem.y = this.trainLables;

		return problem;
	}

	public svm_parameter setParam() {
		svm_parameter param = new svm_parameter();
		param.svm_type = this.svmType;
		param.kernel_type = this.kernelType;
		param.cache_size = this.cacheSize;
		param.eps = this.eps;
		param.C = this.C;
		return param;
	}

	public void trainModel() {
		svm_problem problem = setProblem();
		svm_parameter param = setParam();

		if (svm.svm_check_parameter(problem, param) == null) {
			model = svm.svm_train(problem, param);

			try {
				svm.svm_save_model(this.modelFilePath, model);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Logger.getGlobal().logp(Level.WARNING, this.getClass().getName(),
					"LibSvmModel", "参数设置！");
		}
	}

	public double predict(svm_node[] feature) {
		return svm.svm_predict(this.model, feature);
	}

	public void testModel() {
		if (this.model == null) {
			try {
				this.model = svm.svm_load_model(this.modelFilePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (this.testPoint != null && this.testLables != null) {
			for (int i = 0; i < this.testPoint.length; i++) {
				@SuppressWarnings("unused")
				double score = predict(this.testPoint[i]);
			}
		}
	}

	@SuppressWarnings("unused")
	public double testInstance() {
		if (this.model == null) {
			try {
				this.model = svm.svm_load_model(this.modelFilePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (this.testPoint != null && this.testLables != null) {
			for (int i = 0; i < this.testPoint.length; i++) {
				double score = predict(this.testPoint[i]);
				return score;
			}
		}
		return -1.0;
	}
}
