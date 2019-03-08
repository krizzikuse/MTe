package com.mercury.platform.ui.components.panel.notification;


import com.mercury.platform.shared.config.descriptor.ResponseButtonDescriptor;
import com.mercury.platform.shared.entity.message.TradeNotificationDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.notification.controller.NotificationController;
import com.mercury.platform.ui.frame.other.ChatHistoryDefinition;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import currencydata.CurrencyAmount;
import currencydata.CurrencyData;
import rx.Subscription;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import net.miginfocom.swing.MigLayout;

public abstract class TradeNotificationPanel<T extends TradeNotificationDescriptor, C extends NotificationController> extends NotificationPanel<T, C> {
    protected JPanel responseButtonsPanel;
    //protected JPanel testPanel;
    protected JLabel nicknameLabel;

    private Subscription chatSubscription;
    private Subscription playerJoinSubscription;
    private Subscription playerLeaveSubscription;
    @Override
    public void onViewInit() {
        super.onViewInit();
        //this.testPanel = this.componentsFactory.getJPanel(new BorderLayout(),AppThemeColor.FRAME);//BorderLayout., 5, 2), AppThemeColor.FRAME);
        this.responseButtonsPanel = this.componentsFactory.getJPanel(new FlowLayout(FlowLayout.CENTER, 5, 2), AppThemeColor.FRAME);
        //testPanel.add(responseButtonsPanel,BorderLayout.SOUTH);
        this.contentPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.FRAME);
//        GridBagConstraints c = new GridBagConstraints();
//        c.gridx = 0;
//        c.gridy = 2;
//        c.weightx = 1.0;
//        c.weighty = 0.0;
//        c.ipady = -20;
//        c.anchor = GridBagConstraints.LAST_LINE_START;
//        c.fill = GridBagConstraints.HORIZONTAL;        
//        this.contentPanel = this.componentsFactory.getJPanel(new GridBagLayout(), AppThemeColor.FRAME);
        switch (notificationConfig.get().getFlowDirections()) {
            case DOWNWARDS: {
                this.add(this.getHeader(), BorderLayout.PAGE_START);
                //this.contentPanel.add(this.testPanel, BorderLayout.PAGE_END);
                this.contentPanel.add(this.responseButtonsPanel, BorderLayout.PAGE_END);
                //this.contentPanel.add(this.responseButtonsPanel,c);// BorderLayout.PAGE_END);
                break;
            }
            case UPWARDS: {
                this.add(this.getHeader(), BorderLayout.PAGE_END);
                //this.contentPanel.add(this.testPanel, BorderLayout.PAGE_START);
                this.contentPanel.add(this.responseButtonsPanel, BorderLayout.PAGE_START);
                //this.contentPanel.add(this.responseButtonsPanel,c);// BorderLayout.PAGE_START);
                break;
            }
        }
        //c = new GridBagConstraints();
        //c.
        this.contentPanel.add(this.getMessagePanel(), BorderLayout.CENTER);
//        this.contentPanel.add(this.getMessagePanel(), c);
//        this.contentPanel.add(this.getMessagePanel(), c);//BorderLayout.CENTER);
        this.add(this.contentPanel, BorderLayout.CENTER);
        this.updateHotKeyPool();
    }

    protected abstract JPanel getHeader();

    protected abstract JPanel getMessagePanel();

    protected void initResponseButtonsPanel(List<ResponseButtonDescriptor> buttonsConfig) {
        this.responseButtonsPanel.removeAll();
        Collections.sort(buttonsConfig);
        buttonsConfig.forEach((buttonConfig) -> {
            JButton button = componentsFactory.getBorderedButton(buttonConfig.getTitle(), 16f, AppThemeColor.RESPONSE_BUTTON, AppThemeColor.RESPONSE_BUTTON_BORDER, AppThemeColor.RESPONSE_BUTTON);
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(AppThemeColor.RESPONSE_BUTTON_BORDER, 1),
                    //BorderFactory.createMatteBorder(3, 9, 3, 9, AppThemeColor.RESPONSE_BUTTON)
                    BorderFactory.createMatteBorder(0, 9, 0, 9, AppThemeColor.RESPONSE_BUTTON)
            ));
            //Dimension regularSize = button.getSize();
            //regularSize.se
            //button.setSize((int)regularSize.getWidth(), (int)(regularSize.getHeight()*2));
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(AppThemeColor.ADR_SELECTED_BORDER, 1),
                            //BorderFactory.createMatteBorder(3, 9, 3, 9, AppThemeColor.RESPONSE_BUTTON)
                            BorderFactory.createMatteBorder(0, 9, 0, 9, AppThemeColor.RESPONSE_BUTTON)
                    ));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(AppThemeColor.RESPONSE_BUTTON_BORDER, 1),
                            //BorderFactory.createMatteBorder(3, 9, 3, 9, AppThemeColor.RESPONSE_BUTTON)
                            BorderFactory.createMatteBorder(0, 9, 0, 9, AppThemeColor.RESPONSE_BUTTON)
                    ));
                }
            });
            button.addActionListener(action -> {
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(AppThemeColor.ADR_SELECTED_BORDER, 1),
                        //BorderFactory.createMatteBorder(3, 9, 3, 9, AppThemeColor.RESPONSE_BUTTON)
                        BorderFactory.createMatteBorder(0, 9, 0, 9, AppThemeColor.RESPONSE_BUTTON)
                ));
            });
            button.addActionListener(e -> {
                this.controller.performResponse(buttonConfig.getResponseText());
                if (buttonConfig.isClose()) {
                    this.controller.performHide();
                }
            });
            this.hotKeysPool.put(buttonConfig.getHotKeyDescriptor(), button);
            
//            Dimension regularSize =  this.responseButtonsPanel.getSize();
//            int x = (int)regularSize.getWidth();
//            int y = (int)regularSize.getHeight();
//            System.out.println("x="+x+";y="+y+";\n(int)(x*0.5=)"+x*0.5);
//            regularSize = button.getSize();
//            x = (int)regularSize.getWidth();
//            y = (int)regularSize.getHeight();
//            System.out.println("x="+x+";y="+y+";\n(int)(x*0.5=)"+x*0.5);
//            button.setHorizontalAlignment(SwingConstants.LEFT);
//            button.setVerticalAlignment(SwingConstants.BOTTOM);
//            button.setHorizontalTextPosition(SwingConstants.LEFT);
//            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            //button.setPreferredSize(regularSize);
            //this.responseButtonsPanel.setMaximumSize(new Dimension(x,((int)(y*0.5))));
            //this.responseButtonsPanel.setPreferredSize(new Dimension(200,((int)(25*(this.componentsFactory.getScale())))));
            //this.responseButtonsPanel.setPreferredSize(new Dimension(x,((int)(y*0.5))));
            //button.setPreferredSize(new Dimension(200,((int)(25*(this.componentsFactory.getScale())))));
            //System.out.println(button.getSize());
            this.responseButtonsPanel.add(button);
        });
    }

    @Override
    public void subscribe() {
        super.subscribe();
        this.chatSubscription = MercuryStoreCore.plainMessageSubject.subscribe(message -> {
            if (this.data.getWhisperNickname().equals(message.getNickName())) {
                this.data.getRelatedMessages().add(message);
            }
        });
        this.playerJoinSubscription = MercuryStoreCore.playerJoinSubject.subscribe(nickname -> {
            if (this.data.getWhisperNickname().equals(nickname)) {
                this.nicknameLabel.setForeground(AppThemeColor.TEXT_SUCCESS);
            }
        });
        this.playerLeaveSubscription = MercuryStoreCore.playerLeftSubject.subscribe(nickname -> {
            if (this.data.getWhisperNickname().equals(nickname)) {
                this.nicknameLabel.setForeground(AppThemeColor.TEXT_DISABLE);
            }
        });
    }

    @Override
    public void onViewDestroy() {
        super.onViewDestroy();
        this.chatSubscription.unsubscribe();
        this.playerLeaveSubscription.unsubscribe();
        this.playerJoinSubscription.unsubscribe();
    }

    protected JLabel getHistoryButton() {
        JLabel chatHistory = componentsFactory.getIconLabel("app/chat_history.png", 15);
        chatHistory.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        chatHistory.addMouseListener(new MouseAdapter() {
            Border prevBorder;

            @Override
            public void mouseEntered(MouseEvent e) {
                prevBorder = chatHistory.getBorder();
                chatHistory.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(AppThemeColor.ADR_SELECTED_BORDER),
                        BorderFactory.createEmptyBorder(3, 3, 3, 3)));
                chatHistory.setCursor(new Cursor(Cursor.HAND_CURSOR));
                MercuryStoreUI.showChatHistorySubject.onNext(new ChatHistoryDefinition(data.getRelatedMessages(), e.getLocationOnScreen()));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                chatHistory.setBorder(prevBorder);
                chatHistory.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                MercuryStoreUI.hideChatHistorySubject.onNext(true);
            }
        });
        return chatHistory;
    }

    protected JPanel getForPanel(String signIconPath) {
        JPanel forPanel = new JPanel(new BorderLayout());
        forPanel.setBackground(AppThemeColor.MSG_HEADER);
        JLabel separator = componentsFactory.getIconLabel(signIconPath, 16);
        forPanel.add(separator, BorderLayout.LINE_START);
        separator.setHorizontalAlignment(SwingConstants.CENTER);
        //!!!TODO!!! move currencyLabel + icon to frame below -> done, besides for Currency Trades
//        JPanel currencyPanel = this.getCurrencyPanel(this.data.getCurCount(), this.data.getCurrency());
//        if (currencyPanel != null) {
//            forPanel.add(currencyPanel, BorderLayout.CENTER);
//        }
        return forPanel;
    }

    protected JLabel getTradeDirectionArrow(String signIconPath) {
        //JPanel forPanel = new JPanel(new BorderLayout());
        //forPanel.setBackground(AppThemeColor.MSG_HEADER);
        JLabel arrow = componentsFactory.getIconLabel(signIconPath, 16);
        //forPanel.add(separator, BorderLayout.LINE_START);
        arrow.setHorizontalAlignment(SwingConstants.CENTER);
        arrow.setVerticalAlignment(SwingConstants.CENTER);
        return arrow;
    }

    protected JPanel getCurrencyPanel(Double curCount, String curIconPath) {
        String curCountStr = " ";
        if (curCount > 0) {
            curCountStr = curCount % 1 == 0 ?
                    String.valueOf(curCount.intValue()) :
                    String.valueOf(curCount);
        }
        if (!Objects.equals(curCountStr, "") && curIconPath != null) {
            JLabel currencyLabel = componentsFactory.getIconLabel("currency/" + curIconPath + ".png", 26);
            JPanel curPanel = this.componentsFactory.getJPanel(new GridLayout(1, 0, 4, 0), AppThemeColor.MSG_HEADER);
            curPanel.setAlignmentX(SwingConstants.LEFT);
            JLabel countLabel = this.componentsFactory.getTextLabel(curCountStr, FontStyle.BOLD, 17f);
            countLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            curPanel.add(countLabel);
            curPanel.add(currencyLabel);
            return curPanel;
        }
        return null;
    }

    protected JPanel getCurrencyPanel2(Double curCount, String curIconPath) {
        String curCountStr = " ";
        if (curCount > 0) {
            curCountStr = curCount % 1 == 0 ?
                    String.valueOf(curCount.intValue()) :
                    String.valueOf(curCount);
        }
        if (!Objects.equals(curCountStr, "") && curIconPath != null) {
            JLabel currencyLabel = componentsFactory.getIconLabel("currency/" + CurrencyData.getMTeCurrencyIconName(curIconPath) + ".png", 26);
            JPanel curPanel = this.componentsFactory.getJPanel(new GridLayout(1, 0, 0, 0), AppThemeColor.MSG_HEADER);
            curPanel.setAlignmentX(SwingConstants.LEFT);
            JLabel countLabel = this.componentsFactory.getTextLabel(curCountStr, FontStyle.BOLD, 16f);
            countLabel.setHorizontalAlignment(SwingConstants.LEFT);
            curPanel.add(countLabel);
            curPanel.add(currencyLabel);
            
            curPanel.setBackground(AppThemeColor.TRANSPARENT);
            
            return curPanel;
        }
        return null;
    }
    

    protected JPanel getOfferPanel() {
        JPanel root = new JPanel(new MigLayout("insets 0 0 0 0, gap 0 0, fill"));
        
//        String offer = this.data.getOffer();
        if (this.data.getCurrencies().size()>0) {
            int y = 0;
            for (CurrencyAmount curr : this.data.getCurrencies()) {
                JLabel currencyLabel = componentsFactory.getIconLabel("currency/" + CurrencyData.getMTeCurrencyIconName(curr.getType()) + ".png", 26);
                JLabel countLabel = this.componentsFactory.getTextLabel(String.valueOf(curr.getAmount()), FontStyle.BOLD, 16f);
                root.add(countLabel,"cell 0 " + y);
                root.add(currencyLabel,"cell 1 " + y);
                y++;
            }
        } else return (getCurrencyPanel2(data.getCurCount(), data.getCurrency()));
        return root;
//        if (offer != null && offer.trim().length() > 0) {
//            JLabel offerLabel = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_DEFAULT, TextAlignment.CENTER, 16f, offer);
//            offerLabel.setHorizontalAlignment(SwingConstants.CENTER);
//            return offerLabel;
//        }
//        return null;
    }    

    protected JLabel getOfferLabel() {
        String offer = this.data.getOffer();
        if (offer != null && offer.trim().length() > 0) {
            JLabel offerLabel = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_DEFAULT, TextAlignment.CENTER, 16f, offer);
            offerLabel.setHorizontalAlignment(SwingConstants.CENTER);
            return offerLabel;
        }
        return null;
    }

    protected String getNicknameText() {
        String whisperNickname = data.getWhisperNickname();
        //String result = whisperNickname + ":";
        String result = whisperNickname;
        if (this.notificationConfig.get().isShowLeague()) {
            if (data.getLeague() != null) {
                String league = data.getLeague().trim();
                if (league.length() == 0) {
                    return result;
                }
                if (league.contains("Hardcore")) {
                    if (league.equals("Hardcore")) {
                        result = "HC " + result;
                    } else {
                        result = String.valueOf(league.split(" ")[1].charAt(0)) + "HC " + result;
                    }
                } else if (league.contains("Standard")) {
                    result = "Standard " + result;
                } else {
                    result = String.valueOf(league.charAt(0)) + "SC " + result;
                }
            }
        }
        return result;
    }
}
