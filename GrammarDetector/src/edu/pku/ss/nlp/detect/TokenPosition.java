package edu.pku.ss.nlp.detect;

import java.util.ArrayList;
import java.util.List;

/**
 * Token位置。 对于一个给定的token序列，不同的index集合对应多种语法.
 * 
 * @author nulooper
 * @date 2015年3月24日
 * @time 下午10:36:35
 */
public class TokenPosition {
	private List<Integer> indexList;

	public TokenPosition(List<Integer> indexList) {
		this.setIndexList(new ArrayList<Integer>());
		for (Integer index : indexList) {
			this.getIndexList().add(index);
		}
	}

	/**
	 * 创建一个表示整体语言单元的TokenPostion对象.
	 * 
	 * @return
	 */
	public static TokenPosition createWhole() {
		return new TokenPosition(-1);
	}

	public TokenPosition(int startIndex, int endIndex) {
		this.setIndexList(new ArrayList<Integer>());
		for (int i = startIndex; i < endIndex; ++i) {
			this.getIndexList().add(i);
		}
	}

	public TokenPosition(int index) {
		this.setIndexList(new ArrayList<Integer>());
		this.getIndexList().add(index);
	}

	public TokenPosition(int[] indexArray) {
		this.setIndexList(new ArrayList<Integer>());
		for (int index : indexArray) {
			this.getIndexList().add(index);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getIndexList() == null) ? 0 : getIndexList().hashCode());
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
		TokenPosition other = (TokenPosition) obj;
		if (getIndexList() == null) {
			if (other.getIndexList() != null)
				return false;
		} else if (!getIndexList().equals(other.getIndexList()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getIndexList().toString();
	}

	public List<Integer> getIndexList() {
		return indexList;
	}

	public void setIndexList(List<Integer> indexList) {
		this.indexList = indexList;
	}

	public static void main(String[] args) {
		TokenPosition tp = TokenPosition.createWhole();
		List<Integer> indexList = tp.getIndexList();
		for (int index : indexList) {
			System.out.println(index);
		}
	}
}
