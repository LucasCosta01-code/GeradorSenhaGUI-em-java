import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.security.SecureRandom;

public class GeradorSenha extends JFrame {

    // Cores MSCODEX - Preto e Vermelho Premium
    private static final Color DARK_BG = new Color(12, 12, 12);
    private static final Color DARK_CARD = new Color(22, 22, 22);
    private static final Color DARK_BORDER = new Color(45, 45, 45);
    private static final Color RED_PRIMARY = new Color(220, 38, 38);
    private static final Color RED_DARK = new Color(153, 27, 27);
    private static final Color RED_LIGHT = new Color(248, 113, 113);
    private static final Color GREEN = new Color(34, 197, 94);
    private static final Color YELLOW = new Color(234, 179, 8);
    private static final Color WHITE = new Color(255, 255, 255);
    private static final Color GRAY = new Color(163, 163, 163);
    private static final Color GRAY_DARK = new Color(82, 82, 82);

    // Caracteres
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMS = "0123456789";
    private static final String SYMS = "!@#$%^&*()_+-=[]{}|;:,.<>?";

    // Componentes
    private JTextField senhaField;
    private JSlider slider;
    private JLabel sizeLabel;
    private JCheckBox chkUpper, chkLower, chkNums, chkSyms;
    private JLabel strengthLabel;
    private JPanel strengthBar;
    private SecureRandom random = new SecureRandom();
    private Point mousePoint;

    public GeradorSenha() {
        setTitle("MSCODEX - Gerador de Senhas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(480, 620);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        JPanel main = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

                // Sombra externa
                for (int i = 0; i < 8; i++) {
                    g2.setColor(new Color(0, 0, 0, 30 - i * 3));
                    g2.fill(new RoundRectangle2D.Double(i, i, getWidth() - i * 2, getHeight() - i * 2, 24, 24));
                }

                // Fundo principal
                g2.setColor(DARK_BG);
                g2.fill(new RoundRectangle2D.Double(4, 4, getWidth() - 8, getHeight() - 8, 20, 20));

                // Borda vermelha brilhante
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(RED_PRIMARY);
                g2.draw(new RoundRectangle2D.Double(5, 5, getWidth() - 10, getHeight() - 10, 18, 18));
            }
        };
        main.setOpaque(false);
        main.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Header
        main.add(createHeader(), BorderLayout.NORTH);

        // Conteudo
        main.add(createContent(), BorderLayout.CENTER);

        setContentPane(main);
        gerarSenha();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradiente vermelho
                GradientPaint gp = new GradientPaint(0, 0, RED_PRIMARY, getWidth(), 0, RED_DARK);
                g2.setPaint(gp);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight() + 15, 14, 14));
            }
        };
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 12));

        // Drag support
        header.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mousePoint = e.getPoint();
            }
        });
        header.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point p = getLocation();
                setLocation(p.x + e.getX() - mousePoint.x, p.y + e.getY() - mousePoint.y);
            }
        });

        // Logo + Titulo
        JPanel titleBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        titleBox.setOpaque(false);

        // Icone M
        JPanel icon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Circulo branco com sombra
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillOval(2, 2, 42, 42);
                g2.setColor(WHITE);
                g2.fillOval(0, 0, 42, 42);

                // Letra M vermelha
                g2.setColor(RED_PRIMARY);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 26));
                FontMetrics fm = g2.getFontMetrics();
                int textW = fm.stringWidth("M");
                int textH = fm.getAscent();
                // Ajuste manual para centralizar verticalmente melhor
                g2.drawString("M", (42 - textW) / 2 + 1, (42 + textH) / 2 - 2);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(44, 44);
            }
        };
        titleBox.add(icon);

        JPanel textBox = new JPanel();
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));
        textBox.setOpaque(false);

        JLabel title = new JLabel("MSCODEX");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(WHITE);
        textBox.add(title);

        JLabel sub = new JLabel("Password Generator");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(new Color(255, 255, 255, 200));
        textBox.add(sub);

        titleBox.add(textBox);
        header.add(titleBox, BorderLayout.WEST);

        // Botoes de controle - AGORA DESENHADOS
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 12));
        controls.setOpaque(false);

        // false = minimizar, true = fechar
        controls.add(createWindowButton(false));
        controls.add(createWindowButton(true));

        header.add(controls, BorderLayout.EAST);
        return header;
    }

    private JPanel createWindowButton(boolean isClose) {
        JPanel btn = new JPanel() {
            boolean hover = false;
            {
                setPreferredSize(new Dimension(36, 36));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setOpaque(false);

                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        hover = true;
                        repaint();
                    }

                    public void mouseExited(MouseEvent e) {
                        hover = false;
                        repaint();
                    }

                    public void mouseClicked(MouseEvent e) {
                        if (isClose)
                            System.exit(0);
                        else
                            setState(Frame.ICONIFIED);
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                // Fundo do botao
                if (hover) {
                    g2.setColor(isClose ? new Color(220, 38, 38, 180) : new Color(255, 255, 255, 40));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }

                // Desenhar Icone
                g2.setColor(WHITE);
                int w = getWidth();
                int h = getHeight();

                if (isClose) {
                    // X icon
                    int size = 10;
                    g2.drawLine(w / 2 - size / 2, h / 2 - size / 2, w / 2 + size / 2, h / 2 + size / 2);
                    g2.drawLine(w / 2 + size / 2, h / 2 - size / 2, w / 2 - size / 2, h / 2 + size / 2);
                } else {
                    // Minimize icon (-)
                    int width = 12;
                    g2.drawLine(w / 2 - width / 2, h / 2, w / 2 + width / 2, h / 2);
                }
            }
        };
        return btn;
    }

    private JPanel createContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Card da senha
        content.add(createPasswordCard());
        content.add(Box.createVerticalStrut(20));

        // Forca
        content.add(createStrengthSection());
        content.add(Box.createVerticalStrut(28));

        // Tamanho
        content.add(createSizeSection());
        content.add(Box.createVerticalStrut(28));

        // Opcoes
        content.add(createOptionsSection());
        content.add(Box.createVerticalStrut(32));

        // Botoes
        content.add(createButtons());

        return content;
    }

    private JPanel createPasswordCard() {
        JPanel card = new JPanel(new BorderLayout(12, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(DARK_CARD);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 14, 14));
                g2.setColor(DARK_BORDER);
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 14, 14));
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 14));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));

        senhaField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        senhaField.setFont(new Font("JetBrains Mono", Font.BOLD, 17));
        if (senhaField.getFont().canDisplay('a') == false) { // Fallback se nao tiver JetBrains
            senhaField.setFont(new Font("Consolas", Font.BOLD, 17));
        }

        senhaField.setForeground(WHITE);
        senhaField.setBackground(DARK_CARD);
        senhaField.setBorder(null);
        senhaField.setEditable(false);
        senhaField.setCaretColor(RED_PRIMARY);
        card.add(senhaField, BorderLayout.CENTER);

        // Botao refresh DESENHADO
        JPanel refreshBtn = createIconButton(38);
        refreshBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                gerarSenha();
            }
        });
        card.add(refreshBtn, BorderLayout.EAST);

        return card;
    }

    private JPanel createIconButton(int size) {
        return new JPanel() {
            boolean hover = false;
            {
                setPreferredSize(new Dimension(size, size));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setOpaque(false);
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        hover = true;
                        repaint();
                    }

                    public void mouseExited(MouseEvent e) {
                        hover = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(2f));

                g2.setColor(hover ? RED_PRIMARY : DARK_BORDER);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10));

                // Desenhar Icone de Refresh
                g2.setColor(WHITE);
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                int r = 7;

                g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                // Arc
                g2.drawArc(cx - r, cy - r, r * 2, r * 2, 45, 270);

                // Arrow head
                // Coordenadas aproximadas para a ponta do arco
                // O arco comeÃ§a em 45 graus (topo direita) e vai 270g anti-horario
                // Termina em 45+270 = 315 graus (4a quadrante, baixo direita)
                // Vamos desenhar a seta no fim do arco
                double angleOfEnd = Math.toRadians(315); // convert to radians
                int ex = cx + (int) (r * Math.cos(angleOfEnd));
                int ey = cy - (int) (r * Math.sin(angleOfEnd)); // Y Ã© invertido

                // Pequena seta manual
                g2.drawLine(ex, ey, ex + 4, ey - 1);
                g2.drawLine(ex, ey, ex + 1, ey + 4);
            }
        };
    }

    private JPanel createStrengthSection() {
        JPanel section = new JPanel(new BorderLayout(12, 0));
        section.setOpaque(false);
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        JLabel label = new JLabel("Forca da Senha");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(GRAY);
        section.add(label, BorderLayout.WEST);

        strengthBar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                g2.setColor(DARK_BORDER);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 8, 8));

                // Barra
                int strength = calcStrength();
                Color c = strength < 35 ? RED_LIGHT : strength < 65 ? YELLOW : GREEN;
                int w = (int) (getWidth() * strength / 100.0);

                GradientPaint gp = new GradientPaint(0, 0, c, w, 0, c.darker());
                g2.setPaint(gp);
                g2.fill(new RoundRectangle2D.Double(0, 0, w, getHeight(), 8, 8));
            }
        };
        strengthBar.setPreferredSize(new Dimension(180, 8));
        section.add(strengthBar, BorderLayout.CENTER);

        strengthLabel = new JLabel("Forte");
        strengthLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        strengthLabel.setForeground(GREEN);
        strengthLabel.setPreferredSize(new Dimension(55, 20));
        strengthLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        section.add(strengthLabel, BorderLayout.EAST);

        return section;
    }

    private JPanel createSizeSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel label = new JLabel("Tamanho da Senha");
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(WHITE);
        top.add(label, BorderLayout.WEST);

        sizeLabel = new JLabel("16 caracteres");
        sizeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sizeLabel.setForeground(RED_PRIMARY);
        top.add(sizeLabel, BorderLayout.EAST);

        section.add(top);
        section.add(Box.createVerticalStrut(14));

        slider = new JSlider(4, 64, 16) {
            {
                setOpaque(false);
                addChangeListener(e -> {
                    sizeLabel.setText(getValue() + " caracteres");
                    gerarSenha();
                });
            }

            @Override
            public void updateUI() {
                setUI(new javax.swing.plaf.basic.BasicSliderUI(this) {
                    @Override
                    public void paintTrack(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        int cy = trackRect.y + trackRect.height / 2 - 4;

                        g2.setColor(DARK_BORDER);
                        g2.fill(new RoundRectangle2D.Double(trackRect.x, cy, trackRect.width, 8, 8, 8));

                        int fill = thumbRect.x - trackRect.x + thumbRect.width / 2;
                        GradientPaint gp = new GradientPaint(trackRect.x, 0, RED_DARK, fill, 0, RED_PRIMARY);
                        g2.setPaint(gp);
                        g2.fill(new RoundRectangle2D.Double(trackRect.x, cy, fill, 8, 8, 8));
                    }

                    @Override
                    public void paintThumb(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        // Sombra
                        g2.setColor(new Color(0, 0, 0, 60));
                        g2.fillOval(thumbRect.x + 2, thumbRect.y + 2, 20, 20);

                        // Circulo branco
                        g2.setColor(WHITE);
                        g2.fillOval(thumbRect.x, thumbRect.y, 20, 20);

                        // Centro vermelho
                        g2.setColor(RED_PRIMARY);
                        g2.fillOval(thumbRect.x + 5, thumbRect.y + 5, 10, 10);
                    }

                    @Override
                    protected Dimension getThumbSize() {
                        return new Dimension(20, 20);
                    }
                });
            }
        };
        section.add(slider);

        return section;
    }

    private JPanel createOptionsSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);

        JLabel label = new JLabel("Caracteres Inclusos");
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(WHITE);
        label.setAlignmentX(LEFT_ALIGNMENT);
        section.add(label);
        section.add(Box.createVerticalStrut(16));

        JPanel grid = new JPanel(new GridLayout(2, 2, 14, 14));
        grid.setOpaque(false);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        chkUpper = createCheck("Maiusculas (A-Z)", true);
        chkLower = createCheck("Minusculas (a-z)", true);
        chkNums = createCheck("Numeros (0-9)", true);
        chkSyms = createCheck("Simbolos (!@#$)", true);

        grid.add(chkUpper);
        grid.add(chkLower);
        grid.add(chkNums);
        grid.add(chkSyms);

        section.add(grid);
        return section;
    }

    private JCheckBox createCheck(String text, boolean sel) {
        JCheckBox chk = new JCheckBox(text, sel) {
            boolean hover = false;
            {
                setOpaque(false);
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        hover = true;
                        repaint();
                    }

                    public void mouseExited(MouseEvent e) {
                        hover = false;
                        repaint();
                    }
                });
                addActionListener(e -> gerarSenha());
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Box
                int y = (getHeight() - 22) / 2;
                g2.setColor(isSelected() ? RED_PRIMARY : (hover ? DARK_BORDER.brighter() : DARK_BORDER));
                g2.fill(new RoundRectangle2D.Double(0, y, 22, 22, 6, 6));

                // Check mark
                if (isSelected()) {
                    g2.setColor(WHITE);
                    g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(5, y + 11, 9, y + 16);
                    g2.drawLine(9, y + 16, 17, y + 7);
                }

                // Texto
                g2.setColor(isSelected() ? WHITE : GRAY);
                g2.setFont(getFont());
                g2.drawString(getText(), 30, (getHeight() + g2.getFontMetrics().getAscent() - 4) / 2);
            }
        };
        return chk;
    }

    private JPanel createButtons() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 16, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));

        panel.add(createButton("Gerar Nova Senha", true, e -> gerarSenha()));
        panel.add(createButton("Copiar Senha", false, e -> copiar()));

        return panel;
    }

    private JPanel createButton(String text, boolean primary, ActionListener action) {
        return new JPanel() {
            boolean hover = false;
            boolean pressed = false;
            String label = text;
            {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setOpaque(false);
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        hover = true;
                        repaint();
                    }

                    public void mouseExited(MouseEvent e) {
                        hover = false;
                        pressed = false;
                        repaint();
                    }

                    public void mousePressed(MouseEvent e) {
                        pressed = true;
                        repaint();
                    }

                    public void mouseReleased(MouseEvent e) {
                        pressed = false;
                        repaint();
                    }

                    public void mouseClicked(MouseEvent e) {
                        action.actionPerformed(null);
                    }
                });
            }

            public void setText(String t) {
                label = t;
                repaint();
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bg = primary ? (hover ? RED_DARK : RED_PRIMARY) : (hover ? DARK_BORDER : DARK_CARD);

                // Sombra
                if (!pressed) {
                    g2.setColor(new Color(0, 0, 0, 40));
                    g2.fill(new RoundRectangle2D.Double(2, 3, getWidth() - 4, getHeight() - 4, 12, 12));
                }

                // Botao
                int offset = pressed ? 2 : 0;
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Double(offset, offset, getWidth() - 4, getHeight() - 4, 12, 12));

                // Borda
                if (!primary) {
                    g2.setColor(DARK_BORDER.brighter());
                    g2.draw(new RoundRectangle2D.Double(offset, offset, getWidth() - 5, getHeight() - 5, 12, 12));
                }

                // Texto
                g2.setColor(WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(label)) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(label, x + offset, y + offset);
            }
        };
    }

    private void gerarSenha() {
        StringBuilder chars = new StringBuilder();
        if (chkUpper != null && chkUpper.isSelected())
            chars.append(UPPER);
        if (chkLower != null && chkLower.isSelected())
            chars.append(LOWER);
        if (chkNums != null && chkNums.isSelected())
            chars.append(NUMS);
        if (chkSyms != null && chkSyms.isSelected())
            chars.append(SYMS);

        if (chars.length() == 0) {
            chars.append(LOWER);
            if (chkLower != null)
                chkLower.setSelected(true);
        }

        int len = slider != null ? slider.getValue() : 16;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        if (senhaField != null)
            senhaField.setText(sb.toString());
        updateStrength();
    }

    private void copiar() {
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(senhaField.getText()), null);
    }

    private int calcStrength() {
        if (senhaField == null)
            return 0;
        String s = senhaField.getText();
        int score = Math.min(s.length() * 3, 35);
        if (s.matches(".*[A-Z].*"))
            score += 15;
        if (s.matches(".*[a-z].*"))
            score += 15;
        if (s.matches(".*[0-9].*"))
            score += 15;
        if (s.matches(".*[^A-Za-z0-9].*"))
            score += 20;
        return Math.min(score, 100);
    }

    private void updateStrength() {
        if (strengthBar != null)
            strengthBar.repaint();
        if (strengthLabel != null) {
            int s = calcStrength();
            strengthLabel.setText(s < 35 ? "Fraca" : s < 65 ? "Media" : "Forte");
            strengthLabel.setForeground(s < 35 ? RED_LIGHT : s < 65 ? YELLOW : GREEN);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
            }
            new GeradorSenha().setVisible(true);
        });
    }
}
