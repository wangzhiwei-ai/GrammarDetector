package edu.pku.ss.nlp.grammar;

/**
 * 语法点基类.
 * 
 * @author nulooper
 * @date 2015年3月24日
 * @time 下午10:36:35
 */
public abstract class BaseGrammar {
	private String name;
	private double confidence = 1.0;

	public BaseGrammar(String name) {
		this.setName(name);
		this.setConfidence(1.0);
	}

	public BaseGrammar(String name, double confidence) {
		this.setName(name);
		this.setConfidence(confidence);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGrammarType() {
		String clsName = this.getClass().getName();

		return clsName.substring(clsName.lastIndexOf(".") + 1);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(confidence);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseGrammar other = (BaseGrammar) obj;
		if (Double.doubleToLongBits(confidence) != Double
				.doubleToLongBits(other.confidence))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getGrammarType() + "[" + name + ", " + confidence + "]";
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		if (confidence < 0) {
			this.confidence = 0.0;
			return;
		}

		if (confidence > 1) {
			this.confidence = 1.0;
			return;
		}

		this.confidence = confidence;
	}
}
