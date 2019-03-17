package com.mercury.platform.ui.components.panel.settings.page;

import com.mercury.platform.shared.CloneHelper;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.HotKeyPair;
import com.mercury.platform.shared.config.descriptor.HotKeysSettingsDescriptor;
import com.mercury.platform.shared.config.descriptor.NotificationSettingsDescriptor;
import com.mercury.platform.shared.entity.message.FlowDirections;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;
import currencydata.ExchangeHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import poedotcom.PoEdotcomLeagueExpert;
import poeninja.PoENinjaCommunicator;

public class NotificationSettingsPagePanel extends SettingsPagePanel {
    private PlainConfigurationService<NotificationSettingsDescriptor> notificationService;
    private PlainConfigurationService<HotKeysSettingsDescriptor> hotKeyService;
    private NotificationSettingsDescriptor generalSnapshot;
    private List<HotKeyPair> incHotKeySnapshot;
    private List<HotKeyPair> outHotKeySnapshot;
    private List<HotKeyPair> scannerHotKeySnapshot;
    private List<HotKeyPair> notificationHotKeySnapshot;

    private HotKeyGroup incHotkeyGroup;
    private HotKeyGroup outHotkeyGroup;
    private HotKeyGroup whHotkeyGroup;
    private HotKeyGroup scannerHotkeyGroup;
    private HotKeyGroup notificationHotkeyGroup;    

    @Override
    public void onViewInit() {
        super.onViewInit();
        this.notificationService = Configuration.get().notificationConfiguration();
        this.hotKeyService = Configuration.get().hotKeysConfiguration();
        this.generalSnapshot = CloneHelper.cloneObject(notificationService.get());
        this.incHotKeySnapshot = CloneHelper.cloneObject(hotKeyService.get().getIncNHotKeysList());
        this.outHotKeySnapshot = CloneHelper.cloneObject(hotKeyService.get().getOutNHotKeysList());
        this.scannerHotKeySnapshot = CloneHelper.cloneObject(hotKeyService.get().getScannerNHotKeysList());
        this.notificationHotKeySnapshot = CloneHelper.cloneObject(hotKeyService.get().getNotificationNHotKeysList());
        

        this.incHotkeyGroup = new HotKeyGroup();
        this.outHotkeyGroup = new HotKeyGroup();
        this.whHotkeyGroup = new HotKeyGroup();
        this.scannerHotkeyGroup = new HotKeyGroup();
        this.notificationHotkeyGroup = new HotKeyGroup();

        JPanel whisperHelperPanel = this.adrComponentsFactory.getCounterPanel(this.getWhisperHelperPanel(), "Whisper helper:", AppThemeColor.ADR_BG, false);
        whisperHelperPanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER));
        JPanel inPanel = this.adrComponentsFactory.getCounterPanel(this.getIncomingPanel(), "Incoming notification:", AppThemeColor.ADR_BG, false);
        inPanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER));
        JPanel outPanel = this.adrComponentsFactory.getCounterPanel(this.getOutgoingPanel(), "Outgoing notification:", AppThemeColor.ADR_BG, false);
        outPanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER));
        JPanel scannerPanel = this.adrComponentsFactory.getCounterPanel(this.getChatScannerPanel(), "Chat scanner notification:", AppThemeColor.ADR_BG, false);
        scannerPanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER));
        this.container.add(this.componentsFactory.wrapToSlide(this.getGeneralPanel(), 4, 4, 2, 4));
        this.container.add(this.componentsFactory.wrapToSlide(whisperHelperPanel, 2, 4, 2, 4));
        this.container.add(this.componentsFactory.wrapToSlide(inPanel, 2, 4, 2, 4));
        this.container.add(this.componentsFactory.wrapToSlide(outPanel, 2, 4, 2, 4));
        this.container.add(this.componentsFactory.wrapToSlide(scannerPanel, 2, 4, 2, 4));
    }

    @Override
    public void onSave() {
        this.notificationService.set(CloneHelper.cloneObject(this.generalSnapshot));
        this.hotKeyService.get().setIncNHotKeysList(CloneHelper.cloneObject(this.incHotKeySnapshot));
        this.hotKeyService.get().setOutNHotKeysList(CloneHelper.cloneObject(this.outHotKeySnapshot));
        this.hotKeyService.get().setScannerNHotKeysList(CloneHelper.cloneObject(this.scannerHotKeySnapshot));
        this.hotKeyService.get().setNotificationNHotKeysList(CloneHelper.cloneObject(this.notificationHotKeySnapshot));
    }

    @Override
    public void restore() {
        this.generalSnapshot = CloneHelper.cloneObject(notificationService.get());
        this.incHotKeySnapshot = CloneHelper.cloneObject(hotKeyService.get().getIncNHotKeysList());
        this.outHotKeySnapshot = CloneHelper.cloneObject(hotKeyService.get().getOutNHotKeysList());
        this.scannerHotKeySnapshot = CloneHelper.cloneObject(hotKeyService.get().getScannerNHotKeysList());
        this.notificationHotKeySnapshot = CloneHelper.cloneObject(hotKeyService.get().getNotificationNHotKeysList());
        this.removeAll();
        this.onViewInit();
    }

    private JPanel getGeneralPanel() {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.ADR_BG);
        JPanel propertiesPanel = this.componentsFactory.getJPanel(new GridLayout(0, 2, 4, 4), AppThemeColor.ADR_BG);
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER));
        JComboBox flowDirectionPicker = componentsFactory.getComboBox(new String[]{"Upwards", "Downwards"});
        flowDirectionPicker.addActionListener(e -> {
            switch ((String) flowDirectionPicker.getSelectedItem()) {
                case "Upwards": {
                    this.generalSnapshot.setFlowDirections(FlowDirections.UPWARDS);
                    break;
                }
                case "Downwards": {
                    this.generalSnapshot.setFlowDirections(FlowDirections.DOWNWARDS);
                    break;
                }
            }
        });
        JLabel limitLabel = this.componentsFactory.getTextLabel(String.valueOf(this.generalSnapshot.getLimitCount()), FontStyle.REGULAR, 16);
        limitLabel.setPreferredSize(new Dimension(30, 26));
        JSlider limitSlider = componentsFactory.getSlider(2, 20, this.generalSnapshot.getLimitCount(), AppThemeColor.ADR_BG);
        limitSlider.addChangeListener(e -> {
            limitLabel.setText(String.valueOf(limitSlider.getValue()));
            this.generalSnapshot.setLimitCount(limitSlider.getValue());
        });
        flowDirectionPicker.setSelectedIndex(this.generalSnapshot.getFlowDirections().ordinal());
        propertiesPanel.add(this.componentsFactory.getTextLabel("Flow direction:", FontStyle.REGULAR, 16));
        propertiesPanel.add(flowDirectionPicker);
        propertiesPanel.add(this.componentsFactory.getTextLabel("Pre-group limit:", FontStyle.REGULAR, 16));
        JPanel limitPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.ADR_BG);
        limitPanel.add(limitLabel, BorderLayout.LINE_START);
        limitPanel.add(limitSlider, BorderLayout.CENTER);
        propertiesPanel.add(limitPanel);
        propertiesPanel.add(this.componentsFactory.getTextLabel("Your nickname(for leave option):", FontStyle.REGULAR, 16));
        JTextField nickNameField = this.componentsFactory.getTextField(this.generalSnapshot.getPlayerNickname(), FontStyle.DEFAULT, 15f);
        nickNameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                generalSnapshot.setPlayerNickname(nickNameField.getText());
            }
        });
        propertiesPanel.add(nickNameField);
        
        propertiesPanel.add(this.componentsFactory.getTextLabel("Your accountname(for price verification):", FontStyle.REGULAR, 16));
        JTextField accountNameField = this.componentsFactory.getTextField(this.generalSnapshot.getAccountName(), FontStyle.DEFAULT, 15f);
        accountNameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                generalSnapshot.setAccountName(accountNameField.getText());
            }
        });        
        propertiesPanel.add(accountNameField);
        
        //!!!!!TODO!!!!! add Field for Hotkey for setting notifications invisible here!
        propertiesPanel.add(this.componentsFactory.getTextLabel("Hotkey for turning Notification invisible:", FontStyle.REGULAR, 16));
        HotKeyPanel hotKeyPanel = new HotKeyPanel(this.notificationHotKeySnapshot.get(0).getDescriptor()); //.getNotificationsInvisibleHotKey());
        this.notificationHotkeyGroup.registerHotkey(hotKeyPanel);
        propertiesPanel.add(hotKeyPanel);
        
        
        ArrayList<JRadioButton> leaguebutton = new ArrayList<JRadioButton>();
        propertiesPanel.add(this.componentsFactory.getTextLabel("Your league for price-verification:", FontStyle.REGULAR, 16));
        JPanel leaguepanel = this.componentsFactory.getJPanel(new FlowLayout(), AppThemeColor.ADR_BG); 
        //leaguebutton.add(new JRadioButton())
        ButtonGroup leagueGroup = new ButtonGroup();
//        group.add(optionLinux);
   
        for(Map.Entry<String,String> league : PoEdotcomLeagueExpert.getLeagues().entrySet()) {
            JRadioButton lb;
            if (league.getValue().equals(this.generalSnapshot.getLeague())) {
                lb = new JRadioButton(league.getKey(),true);
                try {
                    PoENinjaCommunicator poeninjacommhandler;
                    if (!league.getValue().trim().matches("Hardcore\\s.*"))
                        poeninjacommhandler = new PoENinjaCommunicator(league.getValue());
                    else
                        poeninjacommhandler = new PoENinjaCommunicator("tmphardcore");

                    ExchangeHelper.addExchangeRates(poeninjacommhandler.getCurrencyExchangeRates());     
                } catch (Exception ex) {

                }                
                
            } else
                lb = new JRadioButton(league.getKey(),false);
            lb.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent event) {
                    int state = event.getStateChange();
                    if (state == ItemEvent.SELECTED) {
                        generalSnapshot.setLeague(league.getValue());
                        try {
                            PoENinjaCommunicator poeninjacommhandler;
                            if (!league.getValue().trim().matches("Hardcore\\s.*"))
                                poeninjacommhandler = new PoENinjaCommunicator(league.getValue());
                            else
                                poeninjacommhandler = new PoENinjaCommunicator("tmphardcore");

                            ExchangeHelper.addExchangeRates(poeninjacommhandler.getCurrencyExchangeRates());     
                        } catch (Exception ex) {

                        }                        
                        
                    } else if (state == ItemEvent.DESELECTED) {
                        
                    }
                }
            });
            leaguebutton.add(lb);
            leagueGroup.add(lb);
            lb.setBackground(AppThemeColor.TRANSPARENT);
            lb.setForeground(AppThemeColor.TEXT_DEFAULT);
            leaguepanel.add(lb);
        }
        propertiesPanel.add(leaguepanel);
        
//        if (this.selectedProfile.getHotkeysSettingsDescriptor()
//                .getOutNHotKeysList().stream()
//                .filter(it -> HotKeyType.N_WHO_IS.equals(it.getType()))
//                .findAny().orElse(null) == null) {
//            this.selectedProfile.getHotkeysSettingsDescriptor()
//                    .getOutNHotKeysList().add(new HotKeyPair(HotKeyType.N_WHO_IS, new HotKeyDescriptor()));
//        }        
        
        
        
        
        root.add(this.componentsFactory.wrapToSlide(propertiesPanel, AppThemeColor.ADR_BG, 2, 0, 2, 2), BorderLayout.PAGE_START);
        return root;
    }

    private JPanel getWhisperHelperPanel() {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.ADR_BG);

        JPanel parametersPanel = this.componentsFactory.getJPanel(new GridLayout(2, 2), AppThemeColor.ADR_BG);
        JCheckBox enableCheckbox = this.componentsFactory.getCheckBox(this.generalSnapshot.isWhisperHelperEnable());
        enableCheckbox.addActionListener(action -> {
            this.generalSnapshot.setWhisperHelperEnable(enableCheckbox.isSelected());
        });
        parametersPanel.add(this.componentsFactory.getTextLabel("Enabled:", FontStyle.REGULAR, 16));
        parametersPanel.add(enableCheckbox);
        parametersPanel.add(this.componentsFactory.getTextLabel("Hotkey:", FontStyle.REGULAR, 16));
        HotKeyPanel hotKeyPanel = new HotKeyPanel(this.generalSnapshot.getWhisperHelperHotKey());
        this.whHotkeyGroup.registerHotkey(hotKeyPanel);
        parametersPanel.add(hotKeyPanel);

        JPanel showcasePanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.ADR_BG);
        showcasePanel.add(this.componentsFactory.getTextLabel("When you release hotkey button clipboard content will be transferred to chat.", FontStyle.REGULAR, 16), BorderLayout.PAGE_START);
        JLabel img = new JLabel();
        img.setIcon(this.componentsFactory.getImage("app/whisper-helper.png"));
        showcasePanel.add(this.componentsFactory.wrapToSlide(img, AppThemeColor.ADR_BG, 4, 4, 4, 4), BorderLayout.CENTER);
        showcasePanel.add(this.componentsFactory.getTextLabel("Example: press hotkey button => click on 'Whisper' button => release hotkey button.", FontStyle.REGULAR, 16), BorderLayout.PAGE_END);
        root.add(parametersPanel, BorderLayout.PAGE_START);
        root.add(showcasePanel, BorderLayout.CENTER);
        root.setVisible(false);
        return root;
    }
    private JPanel getIncomingPanel() {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.ADR_BG);
        JPanel propertiesPanel = this.componentsFactory.getJPanel(new GridLayout(0, 2, 4, 4), AppThemeColor.ADR_BG);
        JCheckBox enabled = this.componentsFactory.getCheckBox(this.generalSnapshot.isIncNotificationEnable());
        enabled.addActionListener(action -> {
            this.generalSnapshot.setIncNotificationEnable(enabled.isSelected());
        });
        JCheckBox dismiss = this.componentsFactory.getCheckBox(this.generalSnapshot.isDismissAfterKick());
        dismiss.addActionListener(action -> {
            this.generalSnapshot.setDismissAfterKick(dismiss.isSelected());
        });
        JCheckBox showLeague = this.componentsFactory.getCheckBox(this.generalSnapshot.isShowLeague());
        showLeague.addActionListener(action -> {
            this.generalSnapshot.setShowLeague(showLeague.isSelected());
        });
        propertiesPanel.add(this.componentsFactory.getTextLabel("Enabled:", FontStyle.REGULAR, 16));
        propertiesPanel.add(enabled);
        propertiesPanel.add(this.componentsFactory.getTextLabel("Close panel on kick:", FontStyle.REGULAR, 16));
        propertiesPanel.add(dismiss);
        propertiesPanel.add(this.componentsFactory.getTextLabel("Show league:", FontStyle.REGULAR, 16));
        propertiesPanel.add(showLeague);
        root.add(propertiesPanel, BorderLayout.PAGE_START);

        ResponseButtonsPanel responseButtonsPanel = new ResponseButtonsPanel(this.generalSnapshot.getButtons(), this.incHotkeyGroup);
        responseButtonsPanel.onViewInit();
        root.add(this.wrapToCounter(this.componentsFactory.wrapToSlide(responseButtonsPanel, AppThemeColor.ADR_BG), "Response buttons:"), BorderLayout.CENTER);
        root.add(this.wrapToCounter(this.componentsFactory.wrapToSlide(this.getInNotificationHotKeysPanel(), AppThemeColor.ADR_BG), "Hotkeys"), BorderLayout.PAGE_END);
        root.setVisible(false);
        return root;
    }

    private JPanel wrapToCounter(JPanel inner, String title) {
        JPanel root = this.adrComponentsFactory.getCounterPanel(inner, title, AppThemeColor.ADR_BG, false);
        inner.setVisible(false);
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER));
        return this.componentsFactory.wrapToSlide(root, AppThemeColor.ADR_BG);
    }

    private JPanel getInNotificationHotKeysPanel() {
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(0, 2, 4, 4), AppThemeColor.SETTINGS_BG);
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_DEFAULT_BORDER));
        this.incHotKeySnapshot.forEach(pair -> {
            root.add(this.componentsFactory.getIconLabel(pair.getType().getIconPath(), 18, SwingConstants.CENTER, pair.getType().getTooltip()));
            HotKeyPanel hotKeyPanel = new HotKeyPanel(pair.getDescriptor());
            this.incHotkeyGroup.registerHotkey(hotKeyPanel);
            root.add(this.componentsFactory.wrapToSlide(hotKeyPanel, AppThemeColor.SETTINGS_BG, 2, 4, 1, 1));
        });
        return root;
    }
    private JPanel getNotificationHotKeysPanel() {
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(0, 2, 4, 4), AppThemeColor.SETTINGS_BG);
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_DEFAULT_BORDER));
        this.notificationHotKeySnapshot.forEach(pair -> {
            root.add(this.componentsFactory.getIconLabel(pair.getType().getIconPath(), 18, SwingConstants.CENTER, pair.getType().getTooltip()));
            HotKeyPanel hotKeyPanel = new HotKeyPanel(pair.getDescriptor());
            this.incHotkeyGroup.registerHotkey(hotKeyPanel);
            root.add(this.componentsFactory.wrapToSlide(hotKeyPanel, AppThemeColor.SETTINGS_BG, 2, 4, 1, 1));
        });
        return root;
    }

    private JPanel getOutNotificationHotKeysPanel() {
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(0, 2, 4, 4), AppThemeColor.SETTINGS_BG);
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_DEFAULT_BORDER));
        this.outHotKeySnapshot.forEach(pair -> {
            root.add(this.componentsFactory.getIconLabel(pair.getType().getIconPath(), 18, SwingConstants.CENTER, pair.getType().getTooltip()));
            HotKeyPanel hotKeyPanel = new HotKeyPanel(pair.getDescriptor());
            this.outHotkeyGroup.registerHotkey(hotKeyPanel);
            root.add(this.componentsFactory.wrapToSlide(hotKeyPanel, AppThemeColor.SETTINGS_BG, 2, 4, 1, 1));
        });
        return root;
    }

    private JPanel getScannerNotificationHotKeysPanel() {
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(0, 2, 4, 4), AppThemeColor.SETTINGS_BG);
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_DEFAULT_BORDER));
        this.scannerHotKeySnapshot.forEach(pair -> {
            root.add(this.componentsFactory.getIconLabel(pair.getType().getIconPath(), 18, SwingConstants.CENTER, pair.getType().getTooltip()));
            HotKeyPanel hotKeyPanel = new HotKeyPanel(pair.getDescriptor());
            this.scannerHotkeyGroup.registerHotkey(hotKeyPanel);
            root.add(this.componentsFactory.wrapToSlide(hotKeyPanel, AppThemeColor.SETTINGS_BG, 2, 4, 1, 1));
        });
        return root;
    }

    private JPanel getOutgoingPanel() {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.ADR_BG);
        JPanel propertiesPanel = this.componentsFactory.getJPanel(new GridLayout(0, 2, 4, 4), AppThemeColor.ADR_BG);
        JCheckBox enabled = this.componentsFactory.getCheckBox(this.generalSnapshot.isOutNotificationEnable());
        enabled.addActionListener(action -> {
            this.generalSnapshot.setOutNotificationEnable(enabled.isSelected());
        });
        propertiesPanel.add(this.componentsFactory.getTextLabel("Enabled:", FontStyle.REGULAR, 16));
        propertiesPanel.add(enabled);
        propertiesPanel.add(this.componentsFactory.getTextLabel("Close panel after leave:", FontStyle.REGULAR, 16));
        JCheckBox closeAfterLeave = this.componentsFactory.getCheckBox(this.generalSnapshot.isDismissAfterLeave());
        closeAfterLeave.addActionListener(action -> {
            this.generalSnapshot.setDismissAfterLeave(closeAfterLeave.isSelected());
        });
        propertiesPanel.add(closeAfterLeave);
        propertiesPanel.add(this.componentsFactory.getTextLabel("Auto-close triggers:", FontStyle.REGULAR, 16));
        String collect = this.generalSnapshot.getAutoCloseTriggers()
                .stream()
                .collect(Collectors.joining(","));
        JTextField triggersField = this.componentsFactory.getTextField(collect, FontStyle.DEFAULT, 15f);
        triggersField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                generalSnapshot.setAutoCloseTriggers(Arrays.stream(triggersField.getText().split(","))
                        .filter(it -> !it.isEmpty())
                        .collect(Collectors.toList()));
            }
        });
        propertiesPanel.add(triggersField);

        ResponseButtonsPanel responseButtonsPanel = new ResponseButtonsPanel(this.generalSnapshot.getOutButtons(), this.outHotkeyGroup);
        responseButtonsPanel.onViewInit();
        root.add(propertiesPanel, BorderLayout.PAGE_START);
        root.add(this.wrapToCounter(this.componentsFactory.wrapToSlide(responseButtonsPanel, AppThemeColor.ADR_BG), "Response buttons:"), BorderLayout.CENTER);
        root.add(this.wrapToCounter(this.componentsFactory.wrapToSlide(this.getOutNotificationHotKeysPanel(), AppThemeColor.ADR_BG), "Hotkeys:"), BorderLayout.PAGE_END);
        root.setVisible(false);
        return root;
    }

    private JPanel getChatScannerPanel() {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.ADR_BG);
        JPanel propertiesPanel = this.componentsFactory.getJPanel(new GridLayout(0, 2, 4, 4), AppThemeColor.ADR_BG);
        JCheckBox enabled = this.componentsFactory.getCheckBox(this.generalSnapshot.isScannerNotificationEnable());
        enabled.addActionListener(action -> {
            this.generalSnapshot.setScannerNotificationEnable(enabled.isSelected());
        });
        propertiesPanel.add(this.componentsFactory.getTextLabel("Enabled:", FontStyle.REGULAR, 16));
        propertiesPanel.add(enabled);
//        JLabel quickResponseLabel = this.componentsFactory.getIconLabel(HotKeyType.N_QUICK_RESPONSE.getIconPath(), 18);
//        quickResponseLabel.setFont(this.componentsFactory.getFont(FontStyle.REGULAR,16));
//        quickResponseLabel.setForeground(AppThemeColor.TEXT_DEFAULT);
//        quickResponseLabel.setBorder(BorderFactory.createEmptyBorder(0,4,0,0));
//        quickResponseLabel.setText("Response message:");
//        propertiesPanel.add(quickResponseLabel);
//        JTextField quickResponseField = this.componentsFactory.getTextField(this.scannerSnapshot.getResponseMessage(), FontStyle.BOLD, 15f);
//        quickResponseField.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                scannerSnapshot.setResponseMessage(quickResponseField.getText());
//            }
//        });
//        propertiesPanel.add(this.componentsFactory.wrapToSlide(quickResponseField,AppThemeColor.ADR_BG,0,0,0,4));
        root.add(propertiesPanel, BorderLayout.PAGE_START);
        root.add(this.wrapToCounter(this.componentsFactory.wrapToSlide(this.getScannerNotificationHotKeysPanel(), AppThemeColor.ADR_BG), "Hotkeys"), BorderLayout.CENTER);
        root.setVisible(false);
        return root;
    }
}
