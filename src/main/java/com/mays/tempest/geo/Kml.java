package com.mays.tempest.geo;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mays.util.html.ElementW;
import com.mays.util.html.HtmlTag;
import com.mays.util.xml.XmlUtil;

public class Kml {

	private static final Logger logger = LoggerFactory.getLogger(Kml.class);

	private List<KmlPolygon> polygons;

	public void load(String file) throws Exception {
		polygons = new ArrayList<>();
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new File(file));
		List<Node> nodes = doc.selectNodes("//*[local-name() = 'coordinates']");
		logger.info("Nodes: " + nodes.size());
		for (Node node : nodes) {
			Element el = (Element) node;
			KmlPolygon polygon = new KmlPolygon(el.getText());
			polygons.add(polygon);
		}
		logger.info("Polygons: " + polygons.size());
	}

	public List<KmlPolygon> getPolygons() {
		return polygons;
	}

	public Stream<Coordinate> getAllCoordinates() {
		return getPolygons().stream().flatMap(x -> x.getCoordinates().stream());
	}

	public void createPointsKml(Path file, List<KmlPointDisplay> points, String icon) throws Exception {
		Document document = DocumentHelper.createDocument();
		ElementW root = new ElementW(
				document.addElement("kml").addAttribute("xmlns", "http://www.opengis.net/kml/2.2"));
		ElementW doc = root.addElement("Document");
		ElementW style = doc.addElement("Style", "id", "station");
		style.addElement("IconStyle").addElement("Icon").addText(icon);
		for (KmlPointDisplay point : points) {
			ElementW placemark = doc.addElement("Placemark");
			placemark.addElement("styleUrl").addText("#station");
			if (point.getKmlName() != null)
				placemark.addElement("name").addText(point.getKmlName());
			if (point.getKmlDescription() != null) {
				ElementW description = placemark.addElement("description").addText(point.getKmlDescription());
				if (point.getKmlUrl() != null) {
					ElementW html = description.addElement("br");
					html.addElement(HtmlTag.A, "href", point.getKmlUrl()).addText(point.getKmlAnchorText());
					String html_str = html.getElement().asXML();
					html.getElement().detach();
					description.addCDATA(html_str);
				}
			}
			placemark.addElement("Point").addElement("coordinates")
					.addText(point.getKmlCoordinate().getLongitude() + "," + point.getKmlCoordinate().getLatitude());
		}
		XmlUtil.write(document, file);
	}

}
