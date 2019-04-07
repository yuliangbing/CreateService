package com.create.service.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.zptc.gx.permission.entity.Menu;
import com.zptc.gx.permission.entity.Role;
import com.zptc.gx.permission.entity.RoleMenuRel;
import com.zptc.gx.permission.entity.ZptcUser;

public class CreateService {

	private static final String projectName = "logistics"; //妞ゅ湱娲伴崥宥囆�
	
	public static void main(String[] args) throws Exception {
		createCodeFromDO();

	}

	@SuppressWarnings("rawtypes")
	public static void createCodeFromDO() throws Exception {
		List<Class> clsList = new ArrayList<Class>();
		// 閹跺﹪娓剁憰浣烘晸閹存亯omain閺傚洣娆㈤惃鍑濷缁濮為崚鐧搃st娑擄拷

		clsList.add(Menu.class);
		clsList.add(Role.class);
		clsList.add(RoleMenuRel.class);
		clsList.add(ZptcUser.class);

		for (Class cls1 : clsList) {
			String className = cls1.getName();
			Class<?> cls = Class.forName(className);
			/*createPOJO(cls);
			System.out.println("--------pojo閻㈢喐鍨�-----------------");
			createHelper(cls);
			System.out.println("--------helper閻㈢喐鍨�-----------------");*/
			 createRepository(cls);
			 System.out.println("--------repository閻㈢喐鍨�-----------------");
			 createRepositoryImpl(cls);
			 System.out.println("--------repositoryImpl閻㈢喐鍨�-----------------");
			/*createDTO(cls);
			System.out.println("--------dto閻㈢喐鍨�-----------------");
			 createFacade(cls);
			 System.out.println("--------facade閻㈢喐鍨�-----------------");
			 createFacadeImpl(cls);
			 System.out.println("--------facadeImpl閻㈢喐鍨�-----------------");*/
//			createController(cls);
//			System.out.println("--------controller閻㈢喐鍨�-----------------");
		}

	}

	public static File createPackage(String packageName) {
		String str = CreateService.class.getResource("/").getPath();
		str = str.substring(1, str.indexOf("target"));
		String parent = "src/main/java/";
		packageName = packageName.replaceAll("\\.", "/");
		File dir = new File(str + parent + packageName);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	public static void createJavaFile(File dir, String fileName, String content) {
		try {
			File file = new File(dir, fileName + ".java");
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(content);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createRepositoryImpl(Class cls) {
		StringBuffer sb = new StringBuffer();
		String className = cls.getName();
		String packageName = cls.getPackage().getName()
				.replace(".entity", ".service.impl");
		sb.append("package ").append(packageName).append(";\n\n");
		
		File dir = createPackage(packageName);

		String doName = cls.getSimpleName();

		String serviceName = doName + "Service";
		String serviceImplName = serviceName + "Impl";
		sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
		sb.append("import org.springframework.stereotype.Component;\n\n");
		sb.append("import ").append(className).append(";").append("\n");
		String mapperPackageName = cls.getPackage().getName()
				.replace(".entity", ".mapper");
		sb.append("import ").append(mapperPackageName).append(".").append(doName).append("Mapper").append(";\n");
		
		String servicePackageName = cls.getPackage().getName()
				.replace(".entity", ".service");
		sb.append("import ").append(servicePackageName).append(".").append(doName).append("Service").append(";\n\n");
		sb.append("@Component\n");
		sb.append("public class ").append(serviceImplName)
				.append(" implements " + serviceName).append(" {")
				.append("\n\n");

		String mapperName = doName + "Mapper";
		String mapperNameFirstChar = mapperName.substring(0, 1);
		String mapperNameObject = mapperNameFirstChar.toLowerCase()
				+ mapperName.substring(1);

		sb.append("\t@Autowired\n");
		sb.append("\tprivate " + mapperName + " " + mapperNameObject + ";\n\n");

		String pojoNameFirstChar = doName.substring(0, 1);
		String pojoNameObject = pojoNameFirstChar.toLowerCase()
				+ doName.substring(1);

		String doNameFirstChar = doName.substring(0, 1);
		String doNameObject = doNameFirstChar.toLowerCase()
				+ doName.substring(1);

		StringBuffer sb1 = new StringBuffer();
		sb1.append("\t@Override\n");
		sb1.append("\tpublic int add" + doName + "(" + doName + " "
				+ pojoNameObject + "){\n");
		sb1.append("\t\t")
				.append(mapperNameObject + ".insertSelective(" + doNameObject
						+ ");").append("\n");
		sb1.append("\t}\n");
		sb.append(sb1);

		StringBuffer sb2 = new StringBuffer();
		sb2.append("\t@Override\n");
		sb2.append("\tpublic int modify" + doName + "(" + doName + " "
				+ pojoNameObject + "){\n");
		sb2.append("\t\t")
				.append(mapperNameObject + ".updateByPrimaryKeySelective("
						+ doNameObject + ");").append("\n");
		sb2.append("\t}\n");
		sb.append(sb2);

		StringBuffer sb3 = new StringBuffer();
		sb3.append("\t@Override\n");
		sb3.append("\tpublic int delete" + doName + "ById(Long id){\n");
		sb3.append("\t\t")
				.append(mapperNameObject + ".deleteByPrimaryKey(id);")
				.append("\n");
		sb3.append("\t}\n");
		sb.append(sb3);

		StringBuffer sb4 = new StringBuffer();
		sb4.append("\t@Override\n");
		sb4.append("\tpublic " + doName + " find" + doName
				+ "ById(Long id){\n");
		sb4.append("\t\t")
				.append(doName + " " + doNameObject + " = " + mapperNameObject
						+ ".selectByPrimaryKey(id);").append("\n");
		sb4.append("\t\t").append("return " + pojoNameObject + ";")
				.append("\n");
		sb4.append("\t}\n");
		sb.append(sb4);

		sb.append("}\n");

		createJavaFile(dir, serviceImplName, sb.toString());
		// System.out.println(sb);
	}

	public static void createRepository(Class cls) {
		StringBuffer sb = new StringBuffer();

		String packageName = cls.getPackage().getName()
				.replace(".entity", ".service");
		
		String className = cls.getName();
		
		sb.append("package ").append(packageName).append(";\n\n");

		File dir = createPackage(packageName);

		String doName = cls.getSimpleName();
		String serviceName = doName + "Service";
		sb.append("import ").append(className).append(";")
		.append("\n\n");
		sb.append("public interface ").append(serviceName).append(" {")
				.append("\n\n");

		String pojoNameFirstChar = doName.substring(0, 1);
		String pojoNameObject = pojoNameFirstChar.toLowerCase()
				+ doName.substring(1);

		StringBuffer sb1 = new StringBuffer();
		sb1.append("\tpublic int add" + doName + "(" + doName + " "
				+ pojoNameObject + ");\n\n");
		sb.append(sb1);

		StringBuffer sb2 = new StringBuffer();
		sb2.append("\tpublic int modify" + doName + "(" + doName + " "
				+ pojoNameObject + ");\n\n");
		sb.append(sb2);

		StringBuffer sb3 = new StringBuffer();
		sb3.append("\tpublic int delete" + doName + "ById(Long id);\n\n");
		sb.append(sb3);

		StringBuffer sb4 = new StringBuffer();
		sb4.append("\tpublic " + doName + " find" + doName
				+ "ById(Long id);\n\n");
		sb.append(sb4);

		sb.append("}\n");

		createJavaFile(dir, serviceName, sb.toString());
		// System.out.println(sb);
	}

	public static void createHelper(Class cls) {
		StringBuffer sb = new StringBuffer();

		String packageName = cls.getPackage().getName()
				.replace("dal.entity", "domain.helper");
		sb.append("package ").append(packageName).append(";\n\n");

		File dir = createPackage(packageName);

		String doName = cls.getSimpleName();

		String pojoName = doName.replace("DO", "");
		String dtoName = doName.replace("DO", "DTO");
		String helperName = pojoName + "Helper";
		sb.append("public class ").append(helperName).append(" {")
				.append("\n\n");

		String sb1 = createConvertMethod(cls, "convertToDO", pojoName, doName);
		sb.append(sb1);

		String sb2 = createConvertMethod(cls, "convertFromDO", doName, pojoName);
		sb.append(sb2);

		String sb3 = createConvertMethod(cls, "convertToDTO", pojoName, dtoName);
		sb.append(sb3);

		String sb4 = createConvertMethod(cls, "convertFromDTO", dtoName,
				pojoName);
		sb.append(sb4);

		String sb44 = createConvertListMethod(cls, "convertFromDOList",
				pojoName, doName, helperName);
		sb.append(sb44);

		String sb5 = createConvertListMethod(cls, "convertToDOList", doName,
				pojoName, helperName);
		sb.append(sb5);

		String sb6 = createConvertListMethod(cls, "convertToDTOList", dtoName,
				pojoName, helperName);
		sb.append(sb6);

		sb.append("}\n");

		createJavaFile(dir, helperName, sb.toString());
		// System.out.println(sb);

	}

	public static String createConvertListMethod(Class cls, String methodName,
			String pojoName, String doName, String helperName) {
		String convertMethod = methodName.replace("List", "");

		String pojoNameFirstChar = pojoName.substring(0, 1);
		String pojoNameObject = pojoNameFirstChar.toLowerCase()
				+ pojoName.substring(1);

		String doNameFirstChar = doName.substring(0, 1);
		String doNameObject = doNameFirstChar.toLowerCase()
				+ doName.substring(1);

		StringBuffer sb1 = new StringBuffer();
		sb1.append("\tpublic static List<" + pojoName + "> " + methodName
				+ "(List<" + doName + "> " + doNameObject + "List){\n");
		sb1.append("\t\t")
				.append("List<" + pojoName + "> " + pojoNameObject
						+ "List = null;").append("\n");
		sb1.append("\t\t")
				.append("if(!CollectionUtils.isEmpty(" + doNameObject
						+ "List)){").append("\n");
		sb1.append("\t\t\t")
				.append(pojoNameObject + "List = new ArrayList<" + pojoName
						+ ">();").append("\n");
		sb1.append("\t\t\t")
				.append("for(" + doName + " " + doNameObject + ":"
						+ doNameObject + "List){").append("\n");
		sb1.append("\t\t\t\t")
				.append(pojoName + " " + pojoNameObject + " = " + helperName
						+ "." + convertMethod + "(" + doNameObject + ");")
				.append("\n");
		sb1.append("\t\t\t\t")
				.append(pojoNameObject + "List.add(" + pojoNameObject + ");")
				.append("\n");
		sb1.append("\t\t\t").append("}").append("\n");
		sb1.append("\t\t").append("}").append("\n");
		sb1.append("\t\t").append("return " + pojoNameObject + "List;")
				.append("\n");
		sb1.append("\t}\n");
		return sb1.toString();
	}

	public static String createConvertMethod(Class cls, String methodName,
			String pojoName, String doName) {
		String pojoNameFirstChar = pojoName.substring(0, 1);
		String pojoNameObject = pojoNameFirstChar.toLowerCase()
				+ pojoName.substring(1);

		String doNameFirstChar = doName.substring(0, 1);
		String doNameObject = doNameFirstChar.toLowerCase()
				+ doName.substring(1);

		StringBuffer sb1 = new StringBuffer();
		sb1.append("\tpublic static " + doName + " " + methodName + "("
				+ pojoName + " " + pojoNameObject + "){\n");
		sb1.append("\t\t").append("if(" + pojoNameObject + " == null)")
				.append("\n");
		sb1.append("\t\t\t").append("return null;").append("\n");
		sb1.append("\t\t").append("").append("\n");
		sb1.append("\t\t" + doName + " " + doNameObject + " = new " + doName
				+ "()" + ";\n");
		// 閸欐牕绶遍張顒傝閻ㄥ嫬鍙忛柈銊ョ潣閹拷
		Field[] field = cls.getDeclaredFields();
		for (int i = 0; i < field.length; i++) {
			String fieldName = field[i].getName();
			String fieldNameFirstChar = fieldName.substring(0, 1);
			String fieldNameObject = fieldNameFirstChar.toUpperCase()
					+ fieldName.substring(1);
			// 閺夊啴妾烘穱顕�銈扮粭锟�
			int mo = field[i].getModifiers();
			String priv = Modifier.toString(mo);
			// 鐏炵偞锟窖呰閸拷
			Class<?> type = field[i].getType();

			// String lineStr = "\t"+priv + " " + type.getSimpleName() + " " +
			// field[i].getName() + ";\n";
			// sb.append(lineStr);

			sb1.append("\t\t")
					.append(doNameObject + ".set" + fieldNameObject + "("
							+ pojoNameObject + ".get" + fieldNameObject + "())")
					.append(";\n");
		}
		sb1.append("\t\treturn " + doNameObject + ";\n");
		sb1.append("\t}\n");
		return sb1.toString();
	}

	public static void createPOJO(Class cls) {
		StringBuffer sb = new StringBuffer();

		String packageName = cls.getPackage().getName()
				.replace("dal.entity", "domain.pojo");
		sb.append("package ").append(packageName).append(";\n\n");

		File dir = createPackage(packageName);

		String pojoName = cls.getSimpleName().replace("DO", "");
		sb.append("public class ").append(pojoName).append(" {").append("\n");
		// 閸欐牕绶遍張顒傝閻ㄥ嫬鍙忛柈銊ョ潣閹拷
		Field[] field = cls.getDeclaredFields();
		for (int i = 0; i < field.length; i++) {
			// 閺夊啴妾烘穱顕�銈扮粭锟�
			int mo = field[i].getModifiers();
			String priv = Modifier.toString(mo);
			// 鐏炵偞锟窖呰閸拷
			Class<?> type = field[i].getType();

			String lineStr = "\t" + priv + " " + type.getSimpleName() + " "
					+ field[i].getName() + ";\n";
			sb.append(lineStr);
		}
		sb.append("}");

		createJavaFile(dir, pojoName, sb.toString());
		// System.out.println(sb);

	}

	public static void createDTO(Class cls) {
		StringBuffer sb = new StringBuffer();

		String packageName = cls.getPackage().getName()
				.replace("dal.entity", "facade.dto");
		sb.append("package ").append(packageName).append(";\n\n");

		File dir = createPackage(packageName);

		String pojoName = cls.getSimpleName().replace("DO", "DTO");
		sb.append("public class ").append(pojoName).append(" {").append("\n");
		// 閸欐牕绶遍張顒傝閻ㄥ嫬鍙忛柈銊ョ潣閹拷
		Field[] field = cls.getDeclaredFields();
		for (int i = 0; i < field.length; i++) {
			// 閺夊啴妾烘穱顕�銈扮粭锟�
			int mo = field[i].getModifiers();
			String priv = Modifier.toString(mo);
			// 鐏炵偞锟窖呰閸拷
			Class<?> type = field[i].getType();

			String lineStr = "\t" + priv + " " + type.getSimpleName() + " "
					+ field[i].getName() + ";\n";
			sb.append(lineStr);
		}
		sb.append("}");

		createJavaFile(dir, pojoName, sb.toString());
		// System.out.println(sb);
	}

	public static void createFacade(Class cls) {
		StringBuffer sb = new StringBuffer();

		String packageName = cls.getPackage().getName()
				.replace("dal.entity", "facade.inteface");
		sb.append("package ").append(packageName).append(";\n\n");

		File dir = createPackage(packageName);

		String doName = cls.getSimpleName();

		String pojoName = doName.replace("DO", "");
		String dtoName = doName.replace("DO", "DTO");
		String helperName = pojoName + "Helper";
		String facadeName = pojoName + "Facade";
		sb.append("public interface ").append(facadeName).append(" {")
				.append("\n\n");

		String dtoNameFirstChar = dtoName.substring(0, 1);
		String dtoNameObject = dtoNameFirstChar.toLowerCase()
				+ dtoName.substring(1);

		StringBuffer sb1 = new StringBuffer();
		sb1.append("\tpublic void add" + pojoName + "(" + dtoName + " "
				+ dtoNameObject + ");\n\n");
		sb.append(sb1);

		StringBuffer sb2 = new StringBuffer();
		sb2.append("\tpublic void modify" + pojoName + "(" + dtoName + " "
				+ dtoNameObject + ");\n\n");
		sb.append(sb2);

		StringBuffer sb3 = new StringBuffer();
		sb3.append("\tpublic void delete" + pojoName + "ById(Integer id);\n\n");
		sb.append(sb3);

		StringBuffer sb4 = new StringBuffer();
		sb4.append("\tpublic " + dtoName + " find" + pojoName
				+ "ById(Integer id);\n\n");
		sb.append(sb4);

		sb.append("}\n");

		createJavaFile(dir, facadeName, sb.toString());
		// System.out.println(sb);
	}

	public static void createFacadeImpl(Class cls) {
		StringBuffer sb = new StringBuffer();

		String packageName = cls.getPackage().getName()
				.replace("dal.entity", "biz.facadeimpl");
		sb.append("package ").append(packageName).append(";\n\n");

		File dir = createPackage(packageName);

		String doName = cls.getSimpleName();

		String pojoName = doName.replace("DO", "");
		String dtoName = doName.replace("DO", "DTO");
		String helperName = pojoName + "Helper";
		String facadeName = pojoName + "Facade";
		String facadeImplName = facadeName + "Impl";

		sb.append("@Component\n");
		sb.append("public class ").append(facadeImplName)
				.append(" implements " + facadeName).append(" {")
				.append("\n\n");

		String repositoryName = pojoName + "Repository";
		String repositoryNameFirstChar = repositoryName.substring(0, 1);
		String repositoryNameObject = repositoryNameFirstChar.toLowerCase()
				+ repositoryName.substring(1);

		sb.append("\t@Autowired\n");
		sb.append("\tprivate " + repositoryName + " " + repositoryNameObject
				+ ";\n\n");

		String pojoNameFirstChar = pojoName.substring(0, 1);
		String pojoNameObject = pojoNameFirstChar.toLowerCase()
				+ pojoName.substring(1);

		String dtoNameFirstChar = dtoName.substring(0, 1);
		String dtoNameObject = dtoNameFirstChar.toLowerCase()
				+ dtoName.substring(1);

		StringBuffer sb1 = new StringBuffer();
		sb1.append("\t@Override\n");
		sb1.append("\tpublic void add" + pojoName + "(" + dtoName + " "
				+ dtoNameObject + "){\n");
		sb1.append("\t\t")
				.append(pojoName + " " + pojoNameObject + " = " + helperName
						+ ".convertFromDTO(" + dtoNameObject + ");")
				.append("\n");
		sb1.append("\t\t")
				.append(repositoryNameObject + ".add" + pojoName + "("
						+ pojoNameObject + ");").append("\n");
		sb1.append("\t}\n");
		sb.append(sb1);

		StringBuffer sb2 = new StringBuffer();
		sb2.append("\t@Override\n");
		sb2.append("\tpublic void modify" + pojoName + "(" + dtoName + " "
				+ dtoNameObject + "){\n");
		sb2.append("\t\t")
				.append(pojoName + " " + pojoNameObject + " = " + helperName
						+ ".convertFromDTO(" + dtoNameObject + ");")
				.append("\n");
		sb2.append("\t\t")
				.append(repositoryNameObject + ".modify" + pojoName + "("
						+ pojoNameObject + ");").append("\n");
		sb2.append("\t}\n");
		sb.append(sb2);

		StringBuffer sb3 = new StringBuffer();
		sb3.append("\t@Override\n");
		sb3.append("\tpublic void delete" + pojoName + "ById(Integer id){\n");
		sb3.append("\t\t")
				.append(repositoryNameObject + ".delete" + pojoName
						+ "ById(id);").append("\n");
		sb3.append("\t}\n");
		sb.append(sb3);

		StringBuffer sb4 = new StringBuffer();
		sb4.append("\t@Override\n");
		sb4.append("\tpublic " + dtoName + " find" + pojoName
				+ "ById(Integer id){\n");
		sb4.append("\t\t")
				.append(pojoName + " " + pojoNameObject + " = "
						+ repositoryNameObject + ".find" + pojoName
						+ "ById(id);").append("\n");
		sb4.append("\t\t")
				.append(dtoName + " " + dtoNameObject + " = " + helperName
						+ ".convertToDTO(" + pojoNameObject + ");")
				.append("\n");
		sb4.append("\t\t").append("return " + dtoNameObject + ";").append("\n");
		sb4.append("\t}\n");
		sb.append(sb4);

		sb.append("}\n");

		createJavaFile(dir, facadeImplName, sb.toString());
		// System.out.println(sb);
	}

	public static void createController(Class cls) {
		StringBuffer sb = new StringBuffer();
		String packageName = cls.getPackage().getName()
				.replace(".dal.entity", "");
		String modelName = packageName.substring(packageName.lastIndexOf("."));
		packageName = packageName.substring(0, packageName.lastIndexOf("."));
		packageName += "." + projectName + ".web.controller" + modelName;
		sb.append("package ").append(packageName).append(";\n\n");
		
		sb.append("import javax.servlet.http.HttpServletRequest;\n");

		sb.append("import org.apache.log4j.Logger;\n");
		sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
		sb.append("import org.springframework.stereotype.Controller;\n");
		sb.append("import org.springframework.ui.ModelMap;\n");
		sb.append("import org.springframework.web.bind.annotation.RequestMapping;\n");
		sb.append("import org.springframework.web.bind.annotation.ResponseBody;\n");
		
		sb.append("import com.opengroup."+projectName+".web.controller.BaseController;\n");
		sb.append("import com.opengroup."+projectName+".web.util.ToolUtil;\n");
		sb.append("import com.opengroup."+projectName+".web.vo.ResultVO;\n");
		sb.append("\n");
		
		File dir = createPackage(packageName);

		String doName = cls.getSimpleName();
		String pojoName = doName.replace("DO", "");
		String dtoName = doName.replace("DO", "DTO");
		String controllerName = pojoName + "Controller";

		sb.append("@Controller\n");
		sb.append("@RequestMapping(value = \"/" + pojoName.toLowerCase() + "\")\n");
		sb.append("public class ").append(controllerName)
				.append(" extends BaseController").append(" {").append("\n\n");

		String facadeName = pojoName + "Facade";
		String facadeNameFirstChar = facadeName.substring(0, 1);
		String facadeNameObject = facadeNameFirstChar.toLowerCase()
				+ facadeName.substring(1);
		
		sb.append("\t").append("private static Logger logger = Logger.getLogger(").append(controllerName).append(".class);\n\n");

		sb.append("\t").append("@Autowired\n");
		sb.append("\t").append("private " + facadeName + " " + facadeNameObject + ";\n\n");

		// to-add method
		StringBuffer sb1 = new StringBuffer();
		sb1.append("\t").append("@RequestMapping(value = \"/to-add\")\n");
		sb1.append("\t").append("public String toAdd" + pojoName + "(){\n");
		sb1.append("\t\t").append("return \"").append(pojoName.toLowerCase()).append("-add\";\n");
		sb1.append("\t}\n");
		sb.append("\n");
		sb.append(sb1);

		String dtoNameFirstChar = dtoName.substring(0, 1);
		String dtoNameObject = dtoNameFirstChar.toLowerCase()
				+ dtoName.substring(1);

		// add method
		StringBuffer sb2 = new StringBuffer();
		sb2.append("\t").append("@RequestMapping(value = \"/add\")\n");
		sb2.append("\t").append("@ResponseBody\n");
		sb2.append(
				"\t").append("public Object add" + pojoName + "(HttpServletRequest request,")
				.append(dtoName + " ").append(dtoNameObject).append("){\n");
		sb2.append("\t\t").append("ResultVO ret = getRet();\n");
		sb2.append("\t\t").append("try{\n");
		sb2.append("\t\t\t").append("String uniqueId = getCookieId(request);\n");
		sb2.append("\t\t\t").append("String sysFlag = getUserSysFlag(uniqueId);\n");
		sb2.append("\t\t\t").append(dtoNameObject + ".setSys_flag(sysFlag);\n");
		sb2.append("\t\t\t")
				.append(facadeNameObject)
				.append(".add" + pojoName + "(" + dtoNameObject
						+ ");\n\n");
		sb2.append("\t\t").append("}catch(Exception e){\n");
		sb2.append("\t\t\t").append("logger.error(e.getMessage(), e);\n");
		sb2.append("\t\t\t").append("ret.setFail(e.getMessage());\n");
		sb2.append("\t\t").append("}\n");
		sb2.append("\t\t").append("return ret;\n");
		sb2.append("\t}\n");
		sb.append("\n");
		sb.append(sb2);

		// to-edit method
		StringBuffer sb3 = new StringBuffer();
		sb3.append("\t").append("@RequestMapping(value = \"/to-edit\")\n");
		sb3.append("\t").append("public String toEdit" + pojoName
				+ "(ModelMap m, HttpServletRequest request){\n");
		sb3.append("\t\t")
				.append("int id = ToolUtil.integer(\"id\", request);\n");
		sb3.append("\t\t").append(dtoName).append(" ").append(dtoNameObject)
				.append(" = ").append(facadeNameObject).append(".find")
				.append(pojoName).append("ById(id);\n");
		sb3.append("\t\t").append("m.put(\"").append(pojoName).append("\",").append(dtoNameObject).append(");\n");
		sb3.append("\t\t").append("return \"").append(pojoName.toLowerCase()).append("-edit\";\n");
		sb3.append("\t}\n");
		sb.append("\n");
		sb.append(sb3);
		
		// save-edit method
		StringBuffer sb4 = new StringBuffer();
		sb4.append("\t").append("@RequestMapping(value = \"/save\")\n");
		sb4.append("\t").append("@ResponseBody\n");
		sb4.append("\t").append("public Object save" + pojoName + "(HttpServletRequest request,")
				.append(dtoName + " ").append(dtoNameObject).append("){\n");
		sb4.append("\t\t").append("ResultVO ret = getRet();\n");
		sb4.append("\t\t").append("try{\n");
		sb4.append("\t\t\t").append(facadeNameObject).append(".modify").append(pojoName).append("(").append(dtoNameObject).append(");\n");
		sb4.append("\t\t").append("}catch(Exception e){\n");
		sb4.append("\t\t\t").append("logger.error(e.getMessage(), e);\n");
		sb4.append("\t\t\t").append("ret.setFail(e.getMessage());\n");
		sb4.append("\t\t}\n");
		sb4.append("\t\t").append("return ret;\n");
		sb4.append("\t}\n");
		sb.append("\n");
		sb.append(sb4);
		
		//delete method
		StringBuffer sb5 = new StringBuffer();
		sb5.append("\t").append("@RequestMapping(value = \"/del\")\n");
		sb5.append("\t").append("@ResponseBody\n");
		sb5.append("\t").append("public Object delete" + pojoName + "(HttpServletRequest request){\n");
		sb5.append("\t\t").append("ResultVO ret = getRet();\n");
		sb5.append("\t\t").append("try{\n");
		sb5.append("\t\t\t").append("int id = ToolUtil.integer(\"id\", request);\n");
		sb5.append("\t\t\t").append(facadeNameObject).append(".delete").append(pojoName).append("ById(id);\n");
		sb5.append("\t\t").append("}catch(Exception e){\n");
		sb5.append("\t\t\t").append("logger.error(e.getMessage(), e);\n");
		sb5.append("\t\t\t").append("ret.setFail(e.getMessage());\n");
		sb5.append("\t\t}\n");
		sb5.append("\t\t").append("return ret;\n");
		sb5.append("\t}\n");
		sb.append("\n");
		sb.append(sb5);
		
		//list method
		//?
		
		sb.append("}\n");
		
		createJavaFile(dir, controllerName, sb.toString());
	}
}
