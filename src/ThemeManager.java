import com.formdev.flatlaf.*;
import com.formdev.flatlaf.intellijthemes.*;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThemeManager {

    public static String getThemeClass(String themeName) {
        switch (themeName) {
            case "Flat Dark":
                return FlatDarkLaf.class.getName();
            case "IntelliJ":
                return FlatIntelliJLaf.class.getName();
            case "Flat Light":
                return FlatLightLaf.class.getName();
            case "Flat Darcula":
                return FlatDarculaLaf.class.getName();
            case "Arc":
                return FlatArcIJTheme.class.getName();
            case "Arc - Orange":
                return FlatArcOrangeIJTheme.class.getName();
            case "Arc Dark":
                return FlatArcDarkIJTheme.class.getName();
            case "Arc Dark - Orange":
                return FlatArcDarkOrangeIJTheme.class.getName();
            case "Carbon":
                return FlatCarbonIJTheme.class.getName();
            case "Cobalt 2":
                return FlatCobalt2IJTheme.class.getName();
            case "Cyan light":
                return FlatCyanLightIJTheme.class.getName();
            case "Dark Flat":
                return FlatDarkFlatIJTheme.class.getName();
            case "Dark purple":
                return FlatDarkPurpleIJTheme.class.getName();
            case "Dracula":
                return FlatDraculaIJTheme.class.getName();
            case "Gradianto Dark Fuchsia":
                return FlatGradiantoDarkFuchsiaIJTheme.class.getName();
            case "Gradianto Deep Ocean":
                return FlatGradiantoDeepOceanIJTheme.class.getName();
            case "Gradianto Midnight Blue":
                return FlatGradiantoMidnightBlueIJTheme.class.getName();
            case "Gradianto Nature Green":
                return FlatGradiantoNatureGreenIJTheme.class.getName();
            case "Gray":
                return FlatGrayIJTheme.class.getName();
            case "Gruvbox Dark Hard":
                return FlatGruvboxDarkHardIJTheme.class.getName();
            case "Gruvbox Dark Medium":
                return FlatGruvboxDarkMediumIJTheme.class.getName();
            case "Gruvbox Dark Soft":
                return FlatGruvboxDarkSoftIJTheme.class.getName();
            case "Hiberbee Dark":
                return FlatHiberbeeDarkIJTheme.class.getName();
            case "High contrast":
                return FlatHighContrastIJTheme.class.getName();
            case "Light Flat":
                return FlatLightFlatIJTheme.class.getName();
            case "Material Design Dark":
                return FlatMaterialDesignDarkIJTheme.class.getName();
            case "Monocai":
                return FlatMonocaiIJTheme.class.getName();
            case "Monokai Pro":
                return FlatMonokaiProIJTheme.class.getName();
            case "Nord":
                return FlatNordIJTheme.class.getName();
            case "One Dark":
                return FlatOneDarkIJTheme.class.getName();
            case "Solarized Dark":
                return FlatSolarizedDarkIJTheme.class.getName();
            case "Solarized Light":
                return FlatSolarizedLightIJTheme.class.getName();
            case "Spacegray":
                return FlatSpacegrayIJTheme.class.getName();
            case "Vuesion":
                return FlatVuesionIJTheme.class.getName();
            case "Xcode-Dark":
                return FlatXcodeDarkIJTheme.class.getName();
            default:
                System.err.println("Unsupported theme: " + themeName);
                return null;
        }
    }

    public static void setFlatLaf(String themeName) {
        try {
            String themeClass = getThemeClass(themeName);
            if (themeClass != null) {
                try {
                    UIManager.setLookAndFeel(themeClass);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ThemeManager.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(ThemeManager.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(ThemeManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                UIManager.put("Component.focusWidth", 2);
                UIManager.put("Component.innerFocusWidth", 2);
                updateUI();
            }
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Failed to initialize Look and Feel");
        }
    }

    public static void updateUI() {
        SwingUtilities.invokeLater(() -> {
            for (java.awt.Window window : java.awt.Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window);
            }
        });
    }

    public static List<String> getAvailableThemes() {
        return Arrays.asList(
                "Flat Dark", "IntelliJ", "Flat Light", "Flat Darcula",
                "Arc", "Arc - Orange", "Arc Dark", "Arc Dark - Orange",
                "Carbon", "Cobalt 2", "Cyan light", "Dark Flat",
                "Dark purple", "Dracula", "Gradianto Dark Fuchsia", "Gradianto Deep Ocean",
                "Gradianto Midnight Blue", "Gradianto Nature Green", "Gray",
                "Gruvbox Dark Hard", "Gruvbox Dark Medium", "Gruvbox Dark Soft",
                "Hiberbee Dark", "High contrast", "Light Flat", "Material Design Dark",
                "Monocai", "Monokai Pro", "Nord", "One Dark", "Solarized Dark",
                "Solarized Light", "Spacegray", "Vuesion", "Xcode-Dark"
        );
    }
}
