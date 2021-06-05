package ru.vsu.cs;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.*;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;
import ru.vsu.cs.graphs.Graph;
import ru.vsu.cs.graphs.WeightedGraph;
import ru.vsu.cs.graphs.util.GraphUtils;
import ru.vsu.cs.graphs.util.WeightedGraphAlgorithms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.io.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class FrameMain extends JFrame {
    private JPanel panelMain;
    private JPanel graphPainterPanel;
    private JTextArea outputTextArea;
    private JTextArea inputTextArea;
    private JButton loadFromFileButton;
    private JButton buildButton;
    private JButton findPathsBtn;
    private JTextField fromTextField;
    private JTextField toTextField;

    private SvgPanel panelGraphPainter;
    private Graph graph = null;

    private static class SvgPanel extends JPanel {
        private String svg = null;
        private GraphicsNode svgGraphicsNode = null;

        public void paint(String svg) throws IOException {
            String xmlParser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory df = new SAXSVGDocumentFactory(xmlParser);
            SVGDocument doc = df.createSVGDocument(null, new StringReader(svg));
            UserAgent userAgent = new UserAgentAdapter();
            DocumentLoader loader = new DocumentLoader(userAgent);
            BridgeContext ctx = new BridgeContext(userAgent, loader);
            ctx.setDynamicState(BridgeContext.DYNAMIC);
            GVTBuilder builder = new GVTBuilder();
            svgGraphicsNode = builder.build(ctx, doc);

            this.svg = svg;
            repaint();
        }

        @Override
        public void paintComponent(Graphics gr) {
            super.paintComponent(gr);

            if (svgGraphicsNode == null) {
                return;
            }

            double scaleX = this.getWidth() / svgGraphicsNode.getPrimitiveBounds().getWidth();
            double scaleY = this.getHeight() / svgGraphicsNode.getPrimitiveBounds().getHeight();
            double scale = Math.min(scaleX, scaleY);
            AffineTransform transform = new AffineTransform(scale, 0, 0, scale, 0, 0);
            svgGraphicsNode.setTransform(transform);
            Graphics2D g2d = (Graphics2D) gr;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            svgGraphicsNode.paint(g2d);
        }
    }

    public FrameMain() {
        super("Application");
        setContentPane(panelMain);
        setBounds(100, 100, 1200, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        graphPainterPanel.setLayout(new BorderLayout());
        panelGraphPainter = new SvgPanel();
        graphPainterPanel.add(new JScrollPane(panelGraphPainter));

        inputTextArea.setFont(new Font("SansSerif", Font.PLAIN, 20));
        outputTextArea.setFont(new Font("SansSerif", Font.PLAIN, 20));
        fromTextField.setFont(new Font("SansSerif", Font.PLAIN, 20));
        toTextField.setFont(new Font("SansSerif", Font.PLAIN, 20));



        buildButton.addActionListener(e -> {
            try {
                graph = GraphUtils.fromStr(inputTextArea.getText());
                panelGraphPainter.paint(dotToSvg(GraphUtils.toDot(graph)));
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        });

        loadFromFileButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("./files"));
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                String name = chooser.getSelectedFile().getPath();
                try {
                    inputTextArea.setText(new FileReader(name).readAll());
                }
                catch (FileNotFoundException err) {
                    err.printStackTrace();
                }
            }
        });

        findPathsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int from;
                int to;
                try {
                    from = Integer.parseInt(fromTextField.getText());
                    to = Integer.parseInt(toTextField.getText());
                } catch (NumberFormatException err) {
                    err.printStackTrace();
                    return;
                }
                if (graph instanceof WeightedGraph) {
                    ArrayList<Pair<Integer, List<Integer>>> paths =
                            WeightedGraphAlgorithms.getAllPathsWithLength((WeightedGraph) graph, from, to);
                    paths.sort(new Comparator<Pair<Integer, List<Integer>>>() {
                        @Override
                        public int compare(Pair<Integer, List<Integer>> o1, Pair<Integer, List<Integer>> o2) {
                            return o1.getV1().compareTo(o2.getV1());
                        }
                    });
                    showSystemOut(() -> {
                        for (Pair<Integer, List<Integer>> pair: paths) {
                            System.out.printf("Path: %s Distance: %s\n", pair.getV2(), pair.getV1());
                        }
                    });
                }
            }
        });
    }

    private void showSystemOut(Runnable action) {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(baos, true, "UTF-8"));

            action.run();

            outputTextArea.setText(baos.toString("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.setOut(oldOut);
    }

    private static String dotToSvg(String dotSrc) throws IOException {
        MutableGraph g = new Parser().read(dotSrc);
        return Graphviz.fromGraph(g).render(Format.SVG).toString();
    }
}
