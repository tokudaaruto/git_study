package test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.junit.BeforeClass;
import org.junit.Test;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import employ.OutLog;
import user.bean.RegistrantInfo;
import user.parts.RegInfCheck;
import user.parts.RegInfDAO;

public class JUnitTest {

	static RegInfDAO dao;
	private ArrayList<RegistrantInfo> list;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
		System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
		InitialContext ic = new InitialContext();
		ic.createSubcontext("java:");
		ic.createSubcontext("java:comp");
		ic.createSubcontext("java:comp/env");
		ic.createSubcontext("java:comp/env/jdbc");
		MysqlDataSource ds = new MysqlDataSource();
		ds.setUser("root");
		ds.setPassword("root");
		ds.setURL("jdbc:mysql://localhost/task");
		ic.bind("java:comp/env/jdbc/Task", ds);
	}

	@Test
	public void test1() {
		RegInfCheck regInfCheck = new RegInfCheck();
		String name = "0123456789";
		String msg = "";
		regInfCheck.checkName(name);
		assertEquals(msg, regInfCheck.getErrMsg());
	}

	@Test
	public void test2() {
		RegInfCheck regInfCheck = new RegInfCheck();
		String name = "��������������������";
		String msg = "";
		regInfCheck.checkName(name);
		assertEquals(msg, regInfCheck.getErrMsg());
	}

	@Test
	public void test3() {
		RegInfCheck regInfCheck = new RegInfCheck();
		String name = "01234567890";
		String msg = "���O��10���ȓ��œ��͂��Ă��������B<br />";
		regInfCheck.checkName(name);
		assertEquals(msg, regInfCheck.getErrMsg());
	}

	@Test
	public void test4() {
		RegInfCheck regInfCheck = new RegInfCheck();
		String name = "����������������������";
		String msg = "���O��10���ȓ��œ��͂��Ă��������B<br />";
		regInfCheck.checkName(name);
		assertEquals(msg, regInfCheck.getErrMsg());
	}

	@Test
	public void test5() {
		RegInfCheck regInfCheck = new RegInfCheck();
		String age = "16";
		String msg = "";
		regInfCheck.checkAge(age);
		assertEquals(msg, regInfCheck.getErrMsg());
	}

	@Test
	public void test6() {
		RegInfCheck regInfCheck = new RegInfCheck();
		String age = "60";
		String msg = "";
		regInfCheck.checkAge(age);
		assertEquals(msg, regInfCheck.getErrMsg());
	}

	@Test
	public void test7() {
		RegInfCheck regInfCheck = new RegInfCheck();
		String age = "15";
		String msg = "�N���(16-60)�͈̔͂œ��͂��Ă��������B<br />";
		regInfCheck.checkAge(age);
		assertEquals(msg, regInfCheck.getErrMsg());
	}

	@Test
	public void test8() {
		RegInfCheck regInfCheck = new RegInfCheck();
		String age = "61";
		String msg = "�N���(16-60)�͈̔͂œ��͂��Ă��������B<br />";
		regInfCheck.checkAge(age);
		assertEquals(msg, regInfCheck.getErrMsg());
	}

	@Test
	public void test9() {
		RegInfCheck regInfCheck = new RegInfCheck();
		String age = "�P�U";
		String msg = "�N��͐��l(���p)�œ��͂��Ă��������B<br />";
		regInfCheck.checkAge(age);
		assertEquals(msg, regInfCheck.getErrMsg());
	}

	@Test
	public void test10() {
		RegInfCheck regInfCheck = new RegInfCheck();
		String age = "����������";
		String msg = "�N��͐��l(���p)�œ��͂��Ă��������B<br />"
				+"�N���(16-60)�͈̔͂œ��͂��Ă��������B<br />";
		regInfCheck.checkAge(age);
		assertEquals(msg, regInfCheck.getErrMsg());
	}

	@Test
	public void test11() {
		RegInfCheck regInfCheck = new RegInfCheck();
		String age = "16����������";
		String msg = "�N��͐��l(���p)�œ��͂��Ă��������B<br />"
				+"�N���(16-60)�͈̔͂œ��͂��Ă��������B<br />";
		regInfCheck.checkAge(age);
		assertEquals(msg, regInfCheck.getErrMsg());
	}

	@Test
	public void test12() {
		RegInfCheck regInfCheck = new RegInfCheck();
		String id = "999";
		String msg = "";
		regInfCheck.checkId(id);
		assertEquals(msg, regInfCheck.getErrMsg());
	}

	@Test
	public void test13() {
		RegInfCheck regInfCheck = new RegInfCheck();
		String id = "1000";
		String msg = "�o�^�\��ID�i999�j�𒴂��Ă��܂��B�Ǘ��҂ɖ₢���킹�Ă��������B<br />";
		regInfCheck.checkId(id);
		assertEquals(msg, regInfCheck.getErrMsg());
	}

	@Test
	public void test14() {
		dao = new RegInfDAO();
		//�m�F�p�ϐ�
		String[] setId = new String[]{"001", "002", "003","004"};
		String[] setName = new String[] {"��ؑ��Y", "Tommy", "�R�c�Ԏq","�����H����"};
		String[] setAge = new String[]{"35", "25", "30","28"};

		//���s(�}��/�擾)
		dao.insert(setId[3], setName[3], setAge[3]);
		list = dao.getReglist();

		System.out.println("test14");
		for(int i = 0 ; i < list.size() ; i++) {
			String getId = list.get(i).getrId();
			String getName = list.get(i).getrName();
			String getAge = list.get(i).getrAge();
			System.out.println(getId+" "+getName+" "+getAge);

			assertEquals(setId[i], getId);
			assertEquals(setName[i], getName);
			assertEquals(setAge[i], getAge);

		}

		dao.delete("004");
	}

	@Test
	public void test15() {
		dao = new RegInfDAO();
		//�m�F�p�ϐ�
		String[] setId = new String[]{"001", "002", "003"};
		String[] setName = new String[] {"��ؑ��Y", "Michael", "�R�c�Ԏq"};
		String[] setAge = new String[]{"35", "29", "30"};

		//���s�i�X�V/�擾�j
		dao.update(setId[1], setName[1], setAge[1]);
		list = dao.getReglist();

		System.out.println("test15");
		for(int i = 0 ; i < list.size() ; i++) {
			String getId = list.get(i).getrId();
			String getName = list.get(i).getrName();
			String getAge = list.get(i).getrAge();
			System.out.println(getId+" "+getName+" "+getAge);

			assertEquals(setId[i], getId);
			assertEquals(setName[i], getName);
			assertEquals(setAge[i], getAge);

		}

		dao.update("002", "Tommy", "25");

	}

	@Test
	public void test16() {
		String id = "001";

		//�m�F�p�ϐ�
		String[] setId = new String[]{"002", "003"};
		String[] setName = new String[] {"Tommy", "�R�c�Ԏq"};
		String[] setAge = new String[]{"25", "30"};

		//���s
		dao.delete(id);
		list = dao.getReglist();
		list = dao.getReglist();

		System.out.println("test16");
		for(int i = 0 ; i < list.size() ; i++) {
			String getId = list.get(i).getrId();
			String getName = list.get(i).getrName();
			String getAge = list.get(i).getrAge();
			System.out.println(getId+" "+getName+" "+getAge);

			assertEquals(setId[i], getId);
			assertEquals(setName[i], getName);
			assertEquals(setAge[i], getAge);

		}

		dao.insert("001", "��ؑ��Y", "35");

	}

	@Test
	public void test17() {

		//�m�F�p�ϐ�
		String[] setId = new String[]{"001", "002", "003"};
		String[] setName = new String[] {"��ؑ��Y", "Tommy", "�R�c�Ԏq"};
		String[] setAge = new String[]{"35", "25", "30"};

		//		JUnitBeanTest junitBeanTest;
		//		ArrayList<JUnitBeanTest> getList = new ArrayList<JUnitBeanTest>();
		//
		//		for(int i = 0 ; i < setId.length ; i++) {
		//			junitBeanTest = new JUnitBeanTest(setId[i], setName[i], setAge[i]);
		//			getList.add(junitBeanTest);
		//		}

		//���s
		list = dao.getReglist();
		System.out.println("test17");
		for(int i = 0 ; i < list.size() ; i++) {
			String getId = list.get(i).getrId();
			String getName = list.get(i).getrName();
			String getAge = list.get(i).getrAge();
			System.out.println(getId+" "+getName+" "+getAge);

			//			String id = getList.get(i).getId();
			//			String name = getList.get(i).getName();
			//			String age = getList.get(i).getAge();

			//			assertEquals(id, getId);
			//			assertEquals(name, getName);
			//			assertEquals(age, getAge);

			assertEquals(setId[i], getId);
			assertEquals(setName[i], getName);
			assertEquals(setAge[i], getAge);

		}

	}

	@Test
	public void test18() {

		//DB���̃��X�g
		String[] setId = new String[]{"001", "002", "003"};
		String[] setName = new String[] {"��ؑ��Y", "Tommy", "�R�c�Ԏq"};
		String[] setAge = new String[]{"35", "25", "30"};

		for (String id : setId) {
			dao.delete(id);
		}
		//���s
		String errNum = dao.getNextId();

		assertEquals(errNum, "001");

		for (int i = 0 ; i < setId.length ; i++) {
			dao.insert(setId[i], setName[i], setAge[i]);
		}	
	}

	@Test
	public void test19() {
		//OutLog�N���X�𐶐�
		String setLog = ":sample�F�T���v��";
		//log���L�^����ϐ�
		String logpool = null;		//log�t�@�C������擾
		
		//log�o�͂���
		OutLog.outLogDmp(setLog);

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("C:/test/log/log.txt"));
			logpool = br.readLine();
		}catch (FileNotFoundException ex) {
			System.out.println("FileNotFoundException : "+ex.toString());
			System.exit(0);
		}catch (IOException ex) {
			System.err.println("IOException : "+ex.toString());
			System.exit(0);
		}catch (Exception ex) {
			System.err.println("���̑��̃G���[ : "+ex.toString());
			System.exit(0);
		}
		//���O�t�@�C������擾����������𔼊p�X�y�[�X�ŋ�؂��Ĕz��Ɋi�[
		String[] separateLogPool = logpool.split(" ");
		
		//���t�A���ԁA������ŕ�����
		String yyyyMMDD = separateLogPool[0];
		String hhMMSS = separateLogPool[1].replace(setLog, "");
		String logStr = separateLogPool[1].replace(hhMMSS, "");
		
		assertEquals(logStr, setLog);
		
		SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		
		dayFormat.setLenient(false);
		timeFormat.setLenient(false);
		
		try {
		dayFormat.parse(yyyyMMDD);
		timeFormat.parse(hhMMSS);
		} catch (ParseException ex) {
			System.err.println("ParseException:���t�E���Ԃ̓��̓t�H�[�}�b�g������������܂���");
			fail();
		} catch (Exception ex) {
			System.err.println("���̑��G���[");
			fail();
		} finally {
			try {
			br.close();
			File deleteFile = new File("C:\\test\\log\\log.txt");
			deleteFile.delete();
			} catch (IOException ex) {
				System.err.println("IOException : "+ex.toString());
			}
		}
	}

	@Test
	public void test20() {
		//OutLog�N���X�𐶐�
		String setLog = ":12345";
		//log���L�^����ϐ�
		String logpool = null;		//log�t�@�C������擾
		
		//log�o�͂���
		OutLog.outLogDmp(setLog);

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("C:/test/log/log.txt"));
			logpool = br.readLine();
		}catch (FileNotFoundException ex) {
			System.out.println("FileNotFoundException : "+ex.toString());
			System.exit(0);
		}catch (IOException ex) {
			System.err.println("IOException : "+ex.toString());
			System.exit(0);
		}catch (Exception ex) {
			System.err.println("���̑��̃G���[ : "+ex.toString());
			System.exit(0);
		}
		//���O�t�@�C������擾����������𔼊p�X�y�[�X�ŋ�؂��Ĕz��Ɋi�[
		String[] separateLogPool = logpool.split(" ");
		
		//���t�A���ԁA������ŕ�����
		String yyyyMMDD = separateLogPool[0];
		String hhMMSS = separateLogPool[1].replace(setLog, "");
		String logStr = separateLogPool[1].replace(hhMMSS, "");
		
		assertEquals(logStr, setLog);
		
		SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		
		dayFormat.setLenient(false);
		timeFormat.setLenient(false);
		
		try {
		dayFormat.parse(yyyyMMDD);
		timeFormat.parse(hhMMSS);
		} catch (ParseException ex) {
			System.err.println("ParseException:���t�E���Ԃ̓��̓t�H�[�}�b�g������������܂���");
			fail();
		} catch (Exception ex) {
			System.err.println("���̑��G���[");
			fail();
		} finally {
			try {
			br.close();
			File deleteFile = new File("C:\\test\\log\\log.txt");
			deleteFile.delete();
			} catch (IOException ex) {
				System.err.println("IOException : "+ex.toString());
			}
		}
	}
	
}
