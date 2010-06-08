/*
 * $Id$
 * 
 * Universal Password Manager
 * Copyright (C) 2005 Adrian Smith
 *
 * This file is part of Universal Password Manager.
 *   
 * Universal Password Manager is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Universal Password Manager is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Universal Password Manager; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com._17od.upm.gui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import com._17od.upm.database.AccountInformation;
import com._17od.upm.util.Translator;


public class AccountDialog extends EscapeDialog {

    private AccountInformation pAccount;
    private JTextField userId;
    private JPasswordField password;
    private JTextArea notes;
    private JTextField url;
    private JTextField accountName;
    private boolean okClicked = false;
    private ArrayList existingAccounts;
    private JFrame parentWindow;
    private boolean accountChanged = false;
    private char defaultEchoChar;
    
    
    public AccountDialog(AccountInformation account, JFrame parentWindow, boolean readOnly, ArrayList existingAccounts) {
        super(parentWindow, true);

        // Set the title based on weather we've been opened in readonly mode and weather the
        // Account passed in is empty or not
    	String title = null;
    	if (readOnly) {
    		title = Translator.translate("viewAccount");
    	} else if (!readOnly && account.getAccountName().trim().equals("")) {
    		title = Translator.translate("addAccount");
    	} else {
    		title = Translator.translate("editAccount");
    	}
    	setTitle(title);
    	
    		
        this.pAccount = account;
        this.existingAccounts = existingAccounts;
        this.parentWindow = parentWindow;
        
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        Container container = getContentPane();

        //The AccountName Row
        JLabel accountLabel = new JLabel(Translator.translate("account"));
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(10, 10, 10, 10);
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        container.add(accountLabel, c);

        accountName = new JTextField(new String(pAccount.getAccountName()), 20);
        if (readOnly) {
        	accountName.setEditable(false);
        }
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(10, 10, 10, 10);
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        container.add(accountName, c);
        accountName.addFocusListener(new FocusAdapter() {
        	public void focusGained(FocusEvent e) {
        		accountName.selectAll();
        	}
        });


        //Userid Row
        JLabel useridLabel = new JLabel(Translator.translate("userid"));
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(10, 10, 10, 10);
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        container.add(useridLabel, c);
        
        userId = new JTextField(new String(pAccount.getUserId()), 20);
        if (readOnly) {
        	userId.setEditable(false);
        }
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(10, 10, 10, 10);
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        container.add(userId, c);
        userId.addFocusListener(new FocusAdapter() {
        	public void focusGained(FocusEvent e) {
        		userId.selectAll();
        	}
        });
        
        
        //Password Row
        JLabel passwordLabel = new JLabel(Translator.translate("password"));
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(10, 10, 10, 10);
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        container.add(passwordLabel, c);
        
        password = new JPasswordField(new String(pAccount.getPassword()), 20);
        if (readOnly) {
        	password.setEditable(false);
        }
        c.gridx = 1;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(10, 10, 10, 5);
        c.weightx = 1;
        c.weighty = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        container.add(password, c);
        password.addFocusListener(new FocusAdapter() {
        	public void focusGained(FocusEvent e) {
        		password.selectAll();
        	}
        });

        JCheckBox hidePasswordCheckbox = new JCheckBox(Translator.translate("hide"), true);
        defaultEchoChar = password.getEchoChar();
        hidePasswordCheckbox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
            	if (e.getStateChange() == ItemEvent.SELECTED) {
            		password.setEchoChar(defaultEchoChar);
            	} else {
            		password.setEchoChar((char) 0);
            	}
            }
        });
        c.gridx = 2;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(10, 0, 10, 10);
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        container.add(hidePasswordCheckbox, c);

        //URL Row
        JLabel urlLabel = new JLabel(Translator.translate("url"));
        c.gridx = 0;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(10, 10, 10, 10);
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        container.add(urlLabel, c);
        
        url = new JTextField(new String(pAccount.getUrl()), 20);
        if (readOnly) {
        	url.setEditable(false);
        }
        c.gridx = 1;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(10, 10, 10, 10);
        c.weightx = 1;
        c.weighty = 0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        container.add(url, c);
        url.addFocusListener(new FocusAdapter() {
        	public void focusGained(FocusEvent e) {
        		url.selectAll();
        	}
        });
        
        
        //Notes Row
        JLabel notesLabel = new JLabel(Translator.translate("notes"));
        c.gridx = 0;
        c.gridy = 4;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(10, 10, 10, 10);
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        container.add(notesLabel, c);
        
        notes = new JTextArea(new String(pAccount.getNotes()), 10, 20);
        if (readOnly) {
        	notes.setEditable(false);
        }
        JScrollPane notesScrollPane = new JScrollPane(notes);
        c.gridx = 1;
        c.gridy = 4;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(10, 10, 10, 10);
        c.weightx = 0;
        c.weighty = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        container.add(notesScrollPane, c);
        notes.addFocusListener(new FocusAdapter() {
        	public void focusGained(FocusEvent e) {
        		notes.selectAll();
        	}
        });
        
        
        //Seperator Row
        JSeparator sep = new JSeparator();
        c.gridx = 0;
        c.gridy = 5;
        c.anchor = GridBagConstraints.PAGE_END;
        c.insets = new Insets(0, 0, 0, 0);
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        container.add(sep, c);


        //Button Row
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton(Translator.translate("ok"));
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okButtonAction();
            }
        });
        buttonPanel.add(okButton);
        if (!readOnly) {
	        JButton cancelButton = new JButton(Translator.translate("cancel"));
	        cancelButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                closeButtonAction();
	            }
	        });
	        buttonPanel.add(cancelButton);
        }
        c.gridx = 0;
        c.gridy = 6;
        c.anchor = GridBagConstraints.PAGE_END;
        c.insets = new Insets(5, 0, 5, 0);
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.NONE;
        container.add(buttonPanel, c);
        
    }
    

    public boolean okClicked() {
        return okClicked;
    }
    
    
    public AccountInformation getAccount() {
        return pAccount;
    }
    
    
    private void okButtonAction() {

    	// Check if the account name has changed.
        if (!pAccount.getAccountName().equals(accountName.getText().trim())) {
            accountChanged = true;
        }

        //[1375397] Ensure that an account with the supplied name doesn't already exist.
        //By checking 'accountNames' we're checking both visible and filtered accounts
        //
        // Only check if an account with the same name exists if the account name has actually changed
        if (accountChanged && existingAccounts.indexOf(accountName.getText().trim()) > -1) {
            JOptionPane.showMessageDialog(parentWindow, Translator.translate("accountAlreadyExistsWithName", accountName.getText().trim()), Translator.translate("accountAlreadyExists"), JOptionPane.ERROR_MESSAGE);
        } else {
            // Check for changes
            if (!Arrays.equals(pAccount.getUserId(), userId.getText().getBytes())) {
                accountChanged = true;
            }
            if (!Arrays.equals(pAccount.getPassword(), password.getText().getBytes())) {
                accountChanged = true;
            }
            if (!Arrays.equals(pAccount.getUrl(), url.getText().getBytes())) {
                accountChanged = true;
            }
            if (!Arrays.equals(pAccount.getNotes(), notes.getText().getBytes())) {
                accountChanged = true;
            }

            pAccount.setAccountName(accountName.getText().trim());
            pAccount.setUserId(userId.getText().getBytes());
            pAccount.setPassword(password.getText().getBytes());
            pAccount.setUrl(url.getText().getBytes());
            pAccount.setNotes(notes.getText().getBytes());
            
            setVisible(false);
            dispose();
            okClicked = true;
        }
    }

    
    public boolean getAccountChanged() {
        return accountChanged;
    }
    
    
    private void closeButtonAction() {
        okClicked = false;
        setVisible(false);
        dispose();
    }

}
