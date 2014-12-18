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
		String name = "あいうえおかきくけこ";
		String msg = "";
		regInfCheck.checkName(name);
		assertEquals(msg, regInfCheck.getErrMsg());
	}

	@Test
	public void test3() {
		RegInfCheck regInfCheck = new RegInfCheck();
		String name = "01234567890";
		String msg = "名前は10桁以内で入力してください。<br />";
		regInfCheck.checkName(name);
		assertEquals(msg, regInfCheck.getErrMsg());
	}

	@Test
	public void test4() {
		RegInfCheck regInfCheck = new RegInfCheck();
		String name = "あいうえおかきくけこさ";
		String msg = "名前は10桁以内で入力してください。<br />";
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
		String msg = "年齢は(16-60)の範囲で入力してください。<br />";
		regInfCheck.checkAge(age);
		assertEquals(msg, regInfCheck.getErrMsg());
	}

	@Test
	public void test8() {
		RegInfCheck regInfCheck = new RegInfCheck();
		String age = "61";
		String msg = "年齢は(16-60)の範囲で入力してください。<br />";
		regInfCheck.checkAge(age);
		assertEquals(msg, regInfCheck.getErrMsg());
	}

	@Test
	public void test9() {
		RegInfCheck regInfCheck = new RegInfCheck();
		String age = "１６";
		String msg = "年齢は数値(半角)で入力してください。<br />";
		regInfCheck.checkAge(age);
		assertEquals(msg, regInfCheck.getErrMsg());
	}

	@Test
	public void test10() {
		RegInfCheck regInfCheck = new RegInfCheck();
		String age = "あいうえお";
		String msg = "年齢は数値(半角)で入力してください。<br />"
				+"年齢は(16-60)の範囲で入力してください。<br />";
		regInfCheck.checkAge(age);
		assertEquals(msg, regInfCheck.getErrMsg());
	}

	@Test
	public void test11() {
		RegInfCheck regInfCheck = new RegInfCheck();
		String age = "16あいうえお";
		String msg = "年齢は数値(半角)で入力してください。<br />"
				+"年齢は(16-60)の範囲で入力してください。<br />";
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
		String msg = "登録可能なID（999）を超えています。管理者に問い合わせてください。<br />";
		regInfCheck.checkId(id);
		assertEquals(msg, regInfCheck.getErrMsg());
	}

	@Test
	public void test14() {
		dao = new RegInfDAO();
		//確認用変数
		String[] setId = new String[]{"001", "002", "003","004"};
		String[] setName = new String[] {"鈴木太郎", "Tommy", "山田花子","佐藤路未央"};
		String[] setAge = new String[]{"35", "25", "30","28"};

		//実行(挿入/取得)
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
		//確認用変数
		String[] setId = new String[]{"001", "002", "003"};
		String[] setName = new String[] {"鈴木太郎", "Michael", "山田花子"};
		String[] setAge = new String[]{"35", "29", "30"};

		//実行（更新/取得）
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

		//確認用変数
		String[] setId = new String[]{"002", "003"};
		String[] setName = new String[] {"Tommy", "山田花子"};
		String[] setAge = new String[]{"25", "30"};

		//実行
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

		dao.insert("001", "鈴木太郎", "35");

	}

	@Test
	public void test17() {

		//確認用変数
		String[] setId = new String[]{"001", "002", "003"};
		String[] setName = new String[] {"鈴木太郎", "Tommy", "山田花子"};
		String[] setAge = new String[]{"35", "25", "30"};

		//		JUnitBeanTest junitBeanTest;
		//		ArrayList<JUnitBeanTest> getList = new ArrayList<JUnitBeanTest>();
		//
		//		for(int i = 0 ; i < setId.length ; i++) {
		//			junitBeanTest = new JUnitBeanTest(setId[i], setName[i], setAge[i]);
		//			getList.add(junitBeanTest);
		//		}

		//実行
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

		//DB内のリスト
		String[] setId = new String[]{"001", "002", "003"};
		String[] setName = new String[] {"鈴木太郎", "Tommy", "山田花子"};
		String[] setAge = new String[]{"35", "25", "30"};

		for (String id : setId) {
			dao.delete(id);
		}
		//実行
		String errNum = dao.getNextId();

		assertEquals(errNum, "001");

		for (int i = 0 ; i < setId.length ; i++) {
			dao.insert(setId[i], setName[i], setAge[i]);
		}	
	}

	@Test
	public void test19() {
		//OutLogクラスを生成
		String setLog = ":sample：サンプル";
		//logを記録する変数
		String logpool = null;		//logファイルから取得
		
		//log出力する
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
			System.err.println("その他のエラー : "+ex.toString());
			System.exit(0);
		}
		//ログファイルから取得した文字列を半角スペースで区切って配列に格納
		String[] separateLogPool = logpool.split(" ");
		
		//日付、時間、文字列で分ける
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
			System.err.println("ParseException:日付・時間の入力フォーマットが正しくありません");
			fail();
		} catch (Exception ex) {
			System.err.println("その他エラー");
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
		//OutLogクラスを生成
		String setLog = ":12345";
		//logを記録する変数
		String logpool = null;		//logファイルから取得
		
		//log出力する
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
			System.err.println("その他のエラー : "+ex.toString());
			System.exit(0);
		}
		//ログファイルから取得した文字列を半角スペースで区切って配列に格納
		String[] separateLogPool = logpool.split(" ");
		
		//日付、時間、文字列で分ける
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
			System.err.println("ParseException:日付・時間の入力フォーマットが正しくありません");
			fail();
		} catch (Exception ex) {
			System.err.println("その他エラー");
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
