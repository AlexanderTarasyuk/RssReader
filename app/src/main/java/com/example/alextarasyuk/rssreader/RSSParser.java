package com.example.alextarasyuk.rssreader;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

import com.example.alextarasyuk.model.RSSFeed;
import com.example.alextarasyuk.model.RSSItem;

public class RSSParser {

    private static String TAG_CHANNEL = "channel";
    private static String TAG_TITLE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_DESRIPTION = "description";
    private static String TAG_LANGUAGE = "language";
    private static String TAG_ITEM = "item";
    private static String TAG_PUB_DATE = "pubDate";
    private static String TAG_GUID = "guid";

    public RSSParser() {
    }

    public RSSFeed getRSSFeed(String url) {
        RSSFeed rssFeed = null;
        String rss_feed_xml = null;

        // getting rss link from html source code
        String rss_url = this.getRSSLinkFromURL(url);

        // check if rss_link is found or not
        if (rss_url != null) {
            // RSS url found
            // get RSS XML from rss ulr
            rss_feed_xml = this.getXMLFromUrl(rss_url);
            // check if RSS XML fetched or not
            if (rss_feed_xml != null) {
                // successfully fetched rss xml
                // parse the xml
                try {
                    Document doc = this.getDomElement(rss_feed_xml);
                    NodeList nodeList = doc.getElementsByTagName(TAG_CHANNEL);
                    Element e = (Element) nodeList.item(0);

                    // RSS nodes
                    String title = this.getValue(e, TAG_TITLE);
                    String link = this.getValue(e, TAG_LINK);
                    String description = this.getValue(e, TAG_DESRIPTION);
                    String language = this.getValue(e, TAG_LANGUAGE);

                    // Creating new RSS Feed
                    rssFeed = new RSSFeed(title, description, link, rss_url, language);
                } catch (Exception e) {
                    // Check log for errors
                    e.printStackTrace();
                }

            } else {
                // failed to fetch rss xml
            }
        } else {
            // no RSS url found
        }
        return rssFeed;
    }


    public List<RSSItem> getRSSFeedItems(String rss_url) {
        List<RSSItem> itemsList = new ArrayList<>();
        String rss_feed_xml;

        rss_feed_xml = this.getXMLFromUrl(rss_url);

        if (rss_feed_xml != null) {

            try {
                Document document = this.getDomElement(rss_feed_xml);
                NodeList nodeList = document.getElementsByTagName(TAG_CHANNEL);
                Element e = (Element) nodeList.item(0);
                NodeList items = e.getElementsByTagName(TAG_ITEM);
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element e1 = (Element) items.item(i);

                    String title = this.getValue(e1, TAG_TITLE);
                    String link = this.getValue(e1, TAG_LINK);
                    String description = this.getValue(e1, TAG_DESRIPTION);
                    String pubdate = this.getValue(e1, TAG_PUB_DATE);
                    String guid = this.getValue(e1, TAG_GUID);

                    RSSItem rssItem = new RSSItem(title, link, description, pubdate, guid);

                    // adding item to list
                    itemsList.add(rssItem);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return itemsList;
    }

    private String getRSSLinkFromURL(String url) {

        String rss_url = null;
        try {
            org.jsoup.nodes.Document document = Jsoup.connect(url).get();
            org.jsoup.select.Elements links = document.select("link[type=application/rss+xml]");
            Log.d("No of RSS links found", " " + links.size());

            if (links.size() > 0) {
                rss_url = links.get(0).attr("href").toString();
            } else {
                org.jsoup.select.Elements links1 = document.select("link[type=application/atom+xml]");
                if (links1.size() > 0) {
                    rss_url = links1.get(0).attr("href").toString();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return rss_url;

    }

    private Document getDomElement(String rss_feed_xml) {
        Document doc = null;
        try {

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(rss_feed_xml));
            doc = documentBuilder.parse(inputSource);
        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }
        return doc;
    }

    private String getXMLFromUrl(String url) {

        String xml = null;

        try {
            // request method is GET
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return XML
        return xml;

    }


    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }

    public final String getElementValue(Node elem) {
        Node child;
        if (elem != null) {
            if (elem.hasChildNodes()) {
                for (child = elem.getFirstChild(); child != null; child = child
                        .getNextSibling()) {
                    if (child.getNodeType() == Node.TEXT_NODE || ( child.getNodeType() == Node.CDATA_SECTION_NODE)) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    private String getRSSLinkFromUrl(String url) {
        // RSS url
        String rss_url = null;

        try {
            // Using JSoup library to parse the html source code
            org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
            // finding rss links which are having link[type=application/rss+xml]
            org.jsoup.select.Elements links = doc
                    .select("link[type=application/rss+xml]");

            Log.d("No of RSS links found", " " + links.size());

            // check if urls found or not
            if (links.size() > 0) {
                rss_url = links.get(0).attr("href").toString();
            } else {
                // finding rss links which are having link[type=application/rss+xml]
                org.jsoup.select.Elements links1 = doc
                        .select("link[type=application/atom+xml]");
                if(links1.size() > 0){
                    rss_url = links1.get(0).attr("href").toString();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // returing RSS url
        return rss_url;
    }
}
