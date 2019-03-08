package com.mercury.platform.ui.components.panel.notification;

import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.descriptor.FrameDescriptor;
import com.mercury.platform.shared.config.descriptor.HotKeyPair;
import com.mercury.platform.shared.config.descriptor.HotKeyType;
import com.mercury.platform.shared.entity.message.TradeNotificationDescriptor;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.notification.controller.IncomingPanelController;
import com.mercury.platform.ui.frame.movable.NotificationFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;
import net.miginfocom.swing.MigLayout;


public abstract class TradeIncNotificationPanel<T extends TradeNotificationDescriptor> extends TradeNotificationPanel<T, IncomingPanelController> {
    protected JPanel getHeader() {
        //JPanel root = new JPanel(new BorderLayout());
//        JPanel root = new JPanel(new MigLayout("insets 0 0 0 0, debug"));
        JPanel root = new JPanel(new MigLayout("insets 0 0 0 0, gap 0 0",
        "0[sizegroup,grow]unrel[center]unrel[sizegroup,grow]0",
        "0[]0[]0[]0")); 
        root.setBackground(AppThemeColor.MSG_HEADER);

//        JPanel nickNamePanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.MSG_HEADER);
        this.nicknameLabel = this.componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_NICKNAME, TextAlignment.LEFTOP, 15f, this.getNicknameText());
        nicknameLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 5));
//        nickNamePanel.add(this.getExpandButton(), BorderLayout.LINE_START);
        root.add(this.getExpandButton(),"sgy h, left, grow 0, gapy 0, gapx 0, split 2");
        // following 3 lines worked well. my only gripe is that the name is cut off at character ~11 (...)
//        JPanel headerPanel = this.componentsFactory.getJPanel(new MigLayout(), AppThemeColor.MSG_HEADER);
//        headerPanel.add(this.nicknameLabel,"span 2");
//        headerPanel.add(this.getTradeDirectionArrow("app/incoming_arrow.png"));
        //root.add(this.nicknameLabel,"sgy, span 2, gap 0, wmin 0");
        root.add(this.nicknameLabel,"gapy 0, gapx 0, wmin 0");
        //root.add(this.getTradeDirectionArrow("app/incoming_arrow.png"), "sgy, pos 50% 0");
        root.add(this.getTradeDirectionArrow("app/incoming_arrow.png"), "sgy, wmin 16, split 2, gapright unrel");
        
        //Dimension dim = Configuration.get().framesConfiguration().get(NotificationFrame.class.getSimpleName()).getFrameSize();

        
        //nickNamePanel.add(headerPanel, BorderLayout.CENTER);
        //root.add(nickNamePanel, BorderLayout.CENTER);

//        JPanel opPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.MSG_HEADER);
        //JPanel interactionPanel = new JPanel(new GridLayout(1, 0, 4, 0));
        JPanel interactionPanel = new JPanel(
                new MigLayout("insets 0 0 0 0, fill, gap 0 0",
                            "0[]0",
                            "0[grow,fill]0"));
        interactionPanel.setBackground(AppThemeColor.MSG_HEADER);
        JButton inviteButton = componentsFactory.getIconButton("app/invite.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.INVITE);
        inviteButton.addActionListener(e -> {
            this.controller.performInvite();
            root.setBorder(BorderFactory.createLineBorder(AppThemeColor.HEADER_SELECTED_BORDER));
        });
        JButton kickButton = componentsFactory.getIconButton("app/kick.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.KICK);
        kickButton.addActionListener(e -> {
            this.controller.performKick();
            if (this.notificationConfig.get().isDismissAfterKick()) {
                this.controller.performHide();
            }
        });
        JButton tradeButton = componentsFactory.getIconButton("app/trade.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.TRADE);
        tradeButton.addActionListener(e -> {
            this.controller.performOfferTrade();
        });
        JButton openChatButton = componentsFactory.getIconButton("app/openChat.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.OPEN_CHAT);
        openChatButton.addActionListener(e -> controller.performOpenChat());
        JButton hideButton = componentsFactory.getIconButton("app/close.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.HIDE_PANEL);
        hideButton.addActionListener(action -> {
            this.controller.performHide();
        });
        interactionPanel.add(inviteButton,"split 5, gapx 0");
        interactionPanel.add(tradeButton,"gapx 0");
        interactionPanel.add(kickButton,"gapx 0");
        interactionPanel.add(openChatButton,"gapx 0");
        interactionPanel.add(hideButton,"gapx 0");

        this.interactButtonMap.clear();
        this.interactButtonMap.put(HotKeyType.N_INVITE_PLAYER, inviteButton);
        this.interactButtonMap.put(HotKeyType.N_TRADE_PLAYER, tradeButton);
        this.interactButtonMap.put(HotKeyType.N_KICK_PLAYER, kickButton);
        this.interactButtonMap.put(HotKeyType.N_OPEN_CHAT, openChatButton);
        this.interactButtonMap.put(HotKeyType.N_CLOSE_NOTIFICATION, hideButton);

        JPanel timePanel = this.getTimePanel();
//        opPanel.add(timePanel, BorderLayout.CENTER);
//        opPanel.add(interactionPanel, BorderLayout.LINE_END);
        
        root.add(timePanel,"sgy, wmin 35, gapright push");
        root.add(interactionPanel,"sgy h,right, gap 0 0");
//        root.add(opPanel, BorderLayout.LINE_END);
        return root;
    }


    protected abstract JButton getStillInterestedButton();

    @Override
    protected void updateHotKeyPool() {
        this.hotKeysPool.clear();
        this.interactButtonMap.forEach((type, button) -> {
            HotKeyPair hotKeyPair = this.hotKeysConfig.get()
                    .getIncNHotKeysList()
                    .stream()
                    .filter(it -> it.getType().equals(type))
                    .findAny().orElse(null);
            if (!hotKeyPair.getDescriptor().getTitle().equals("...")) {
                this.hotKeysPool.put(hotKeyPair.getDescriptor(), button);
            }
        });
        this.initResponseButtonsPanel(this.notificationConfig.get().getButtons());
        Window windowAncestor = SwingUtilities.getWindowAncestor(TradeIncNotificationPanel.this);
//            Dimension regularSize =  this.responseButtonsPanel.getSize();
//            
//            int x = (int)regularSize.getWidth();
//            int y = (int)regularSize.getHeight();
//            System.out.println("x="+x+";y="+y+";\n(int)(x*0.5=)"+x*0.5);
//            regularSize = button.getSize();
//            x = (int)regularSize.getWidth();
//            y = (int)regularSize.getHeight();   
//        regularSize = super.responseButtonsPanel.getSize();
//        x = (int)regularSize.getWidth();
//        y = (int)regularSize.getHeight();
//        System.out.println("x="+x+";y="+y+";\n(int)(x*0.5=)"+x*0.5);        
        if (windowAncestor != null) {
            windowAncestor.pack();
        }
    }
}
