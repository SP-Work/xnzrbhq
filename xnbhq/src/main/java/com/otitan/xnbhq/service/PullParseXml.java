package com.otitan.xnbhq.service;

import android.util.Xml;

import com.otitan.xnbhq.entity.QxData;
import com.otitan.xnbhq.entity.Row;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * xml文件解析
 */
public class PullParseXml
{

	/**
	 * @param
	 * @return
	 */
	public List<Row> PullParseXML(InputStream inStream, String tablename)
	{

		List<Row> list = null;
		Row row = null;
		// 构建XmlPullParserFactory
		try
		{
			// XmlPullParserFactory pullParserFactory =
			// XmlPullParserFactory.newInstance();
			// 获取XmlPullParser的实例
			// XmlPullParser xmlPullParser = pullParserFactory.newPullParser();
			XmlPullParser xmlPullParser = Xml.newPullParser();
			// 设置输入流 xml文件
			xmlPullParser.setInput(inStream, "UTF-8");

			// 开始
			int eventType = xmlPullParser.getEventType();
			String tabname = null;
			try
			{
				while (eventType != XmlPullParser.END_DOCUMENT)
				{
					String nodeName = xmlPullParser.getName();
					switch (eventType)
					{
						// 文档开始
						case XmlPullParser.START_DOCUMENT:
							list = new ArrayList<Row>();
							break;
						// 开始节点
						case XmlPullParser.START_TAG:
							// 判断如果其实节点为dic
							if ("dic".equals(nodeName))
							{
								tabname = xmlPullParser.getAttributeValue(0);
							} else if ("row".equals(nodeName)
									&& tablename.equals(tabname))
							{
								row = new Row();
								row.setId(xmlPullParser.getAttributeValue(0));
								row.setName(xmlPullParser.getAttributeValue(1));
								list.add(row);
							}
							break;
						// 结束节点
						case XmlPullParser.END_TAG:
							if ("dic".equals(nodeName) && tablename.equals(tabname))
							{
								row = null;
								return list;
							}
							break;
						default:
							break;
					}
					eventType = xmlPullParser.next();
				}
			} catch (NumberFormatException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		} catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * @param
	 * @return
	 */
	public List<QxData> PullParseXMLQx(InputStream inStream, String tablename)
	{

		List<QxData> list = null;
		QxData row = null;
		// 构建XmlPullParserFactory
		try
		{
			// 获取XmlPullParser的实例
			XmlPullParser xmlPullParser = Xml.newPullParser();
			// 设置输入流 xml文件
			xmlPullParser.setInput(inStream, "UTF-8");

			// 开始
			int eventType = xmlPullParser.getEventType();
			String tabname = null;
			try
			{
				while (eventType != XmlPullParser.END_DOCUMENT)
				{
					String nodeName = xmlPullParser.getName();
					switch (eventType)
					{
						// 文档开始
						case XmlPullParser.START_DOCUMENT:
							list = new ArrayList<QxData>();
							break;
						// 开始节点
						case XmlPullParser.START_TAG:
							// 判断如果其实节点为dic
							if ("dic".equals(nodeName))
							{
								tabname = xmlPullParser.getAttributeValue(0);
							} else if ("row".equals(nodeName) && tablename.equals(tabname))
							{
								row = new QxData();
								row.setQxname(xmlPullParser.getAttributeValue(0));
								row.setDx(xmlPullParser.getAttributeValue(1));
								row.setDy(xmlPullParser.getAttributeValue(2));
								row.setDz(xmlPullParser.getAttributeValue(3));
								row.setDa(xmlPullParser.getAttributeValue(4));
								row.setDf(xmlPullParser.getAttributeValue(5));
								list.add(row);
							}
							break;
						// 结束节点
						case XmlPullParser.END_TAG:
							if ("dic".equals(nodeName) && tablename.equals(tabname))
							{
								row = null;
								return list;
							}
							break;
						default:
							break;
					}
					eventType = xmlPullParser.next();
				}
			} catch (NumberFormatException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		} catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}

		return list;
	}

	public String PullParseXMLforFeildType(InputStream inStream, String name)
	{

		String type = "";
		// 构建XmlPullParserFactory
		try
		{
			// XmlPullParserFactory pullParserFactory =
			// XmlPullParserFactory.newInstance();
			// 获取XmlPullParser的实例
			// XmlPullParser xmlPullParser = pullParserFactory.newPullParser();
			XmlPullParser xmlPullParser = Xml.newPullParser();
			// 设置输入流 xml文件
			xmlPullParser.setInput(inStream, "UTF-8");

			// 开始
			int eventType = xmlPullParser.getEventType();
			String filedname = null;
			try
			{
				while (eventType != XmlPullParser.END_DOCUMENT)
				{
					String nodeName = xmlPullParser.getName();
					switch (eventType)
					{
						// 文档开始
						case XmlPullParser.START_DOCUMENT:
							//list = new ArrayList<Row>();
							break;
						// 开始节点
						case XmlPullParser.START_TAG:
							// 判断如果其实节点为dic
							if ("dic".equals(nodeName))
							{
								filedname = xmlPullParser.getAttributeValue(0);
								if (filedname.equals(name))
								{
									type = xmlPullParser.getAttributeValue(1);
								}
							}
							break;
						// 结束节点
						case XmlPullParser.END_TAG:
							if ("dic".equals(nodeName) && filedname.equals(name))
							{
								return type;
							}
							break;
						default:
							break;
					}
					eventType = xmlPullParser.next();
				}
			} catch (NumberFormatException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		} catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}

		return type;
	}
	/**解析带有强制类型的数据 1为汉字输入 2为数字输入 3为选项输入*/
	public String PullParseXMLforQzlxType(InputStream inStream, String name)
	{

		String qzlx = "";
		// 构建XmlPullParserFactory
		try
		{
			// XmlPullParserFactory pullParserFactory =
			// XmlPullParserFactory.newInstance();
			// 获取XmlPullParser的实例
			// XmlPullParser xmlPullParser = pullParserFactory.newPullParser();
			XmlPullParser xmlPullParser = Xml.newPullParser();
			// 设置输入流 xml文件
			xmlPullParser.setInput(inStream, "UTF-8");

			// 开始
			int eventType = xmlPullParser.getEventType();
			String filedname = null;
			try
			{
				while (eventType != XmlPullParser.END_DOCUMENT)
				{
					String nodeName = xmlPullParser.getName();
					switch (eventType)
					{
						// 文档开始
						case XmlPullParser.START_DOCUMENT:
							// list = new ArrayList<Row>();
							break;
						// 开始节点
						case XmlPullParser.START_TAG:
							// 判断如果其实节点为dic
							if ("dic".equals(nodeName))
							{
								filedname = xmlPullParser.getAttributeValue(0);
								if (filedname.equals(name))
								{
									qzlx = xmlPullParser.getAttributeValue(3);
								}
							}
							break;
						// 结束节点
						case XmlPullParser.END_TAG:
							if ("dic".equals(nodeName) && filedname.equals(name))
							{
								return qzlx;
							}
							break;
						default:
							break;
					}
					eventType = xmlPullParser.next();
				}
			} catch (NumberFormatException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		} catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}

		return qzlx;
	}
	/**解析小数输入 精确小数点后尾数的数据*/
	public String PullParseXMLforXslxType(InputStream inStream, String name)
	{

		String xiaoshu = "";
		// 构建XmlPullParserFactory
		try
		{
			// 获取XmlPullParser的实例
			XmlPullParser xmlPullParser = Xml.newPullParser();
			// 设置输入流 xml文件
			xmlPullParser.setInput(inStream, "UTF-8");

			// 开始
			int eventType = xmlPullParser.getEventType();
			String filedname = null;
			try
			{
				while (eventType != XmlPullParser.END_DOCUMENT)
				{
					String nodeName = xmlPullParser.getName();
					switch (eventType)
					{
						// 文档开始
						case XmlPullParser.START_DOCUMENT:
							// list = new ArrayList<Row>();
							break;
						// 开始节点
						case XmlPullParser.START_TAG:
							// 判断如果其实节点为dic
							if ("dic".equals(nodeName))
							{
								filedname = xmlPullParser.getAttributeValue(0);
								if (filedname.equals(name))
								{
									xiaoshu = xmlPullParser.getAttributeValue(2);
								}
							}
							break;
						// 结束节点
						case XmlPullParser.END_TAG:
							if ("dic".equals(nodeName) && filedname.equals(name))
							{
								return xiaoshu;
							}
							break;
						default:
							break;
					}
					eventType = xmlPullParser.next();
				}
			} catch (NumberFormatException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		} catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}

		return xiaoshu;
	}
	/**
	 * 获取小班号
	 * @param
	 * @return
	 */
	public String PullParseXbhXML(InputStream inStream, String tablename,String id)
	{
		String name="";
		// 构建XmlPullParserFactory
		try
		{
			// XmlPullParserFactory pullParserFactory =
			// XmlPullParserFactory.newInstance();
			// 获取XmlPullParser的实例
			// XmlPullParser xmlPullParser = pullParserFactory.newPullParser();
			XmlPullParser xmlPullParser = Xml.newPullParser();
			// 设置输入流 xml文件
			xmlPullParser.setInput(inStream, "UTF-8");

			// 开始
			int eventType = xmlPullParser.getEventType();
			String tabname = null;
			try
			{
				while (eventType != XmlPullParser.END_DOCUMENT)
				{
					String nodeName = xmlPullParser.getName();
					switch (eventType)
					{
						// 文档开始
						case XmlPullParser.START_DOCUMENT:
							break;
						// 开始节点
						case XmlPullParser.START_TAG:
							// 判断如果其实节点为dic
							if ("dic".equals(nodeName))
							{
								tabname = xmlPullParser.getAttributeValue(0);
							} else if ("row".equals(nodeName)&& tablename.equals(tabname))
							{
								String idname=xmlPullParser.getAttributeValue(0);
								if(idname.equals(id)){
									name=xmlPullParser.getAttributeValue(1);
									break;
								}
							}
							break;
						// 结束节点
						case XmlPullParser.END_TAG:
							if ("dic".equals(nodeName) && tablename.equals(tabname))
							{
								return name;
							}
							break;
						default:
							break;
					}
					eventType = xmlPullParser.next();
				}
			} catch (NumberFormatException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		} catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}

		return name;
	}
}
