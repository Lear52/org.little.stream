package org.little.util.cfg;
import org.little.util.Except;
/**
 * @author av
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface iconfig {
	/**
	 * �������� ���� � ����� ������������
	 * @param sub_cfg - ������ �� ����� ���� ������������
	 * @param topic ������� ���� ([sis.]   sis.basa.)
	 */
	public abstract void open(iconfig sub_cfg, String topic);

	/**
	 * �������� ����� ������������
	 * @param resourse_file - ���� ������������
	 */
	public abstract void open(String resourse_file) throws Except;

	/**
	 * �������� ����� ������������
	 */
	public abstract void close();

	/**
	 * ��������� ������ �� ���������
	 *
	 * @param item - ��������� ������
	 */
	public abstract String get(String item) throws Except;
	public abstract String getTopic();
        public abstract long getLong(String item);
        public abstract int getInt(String item);
}