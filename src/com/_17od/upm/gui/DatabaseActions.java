/*
 * $Id: AccountInformation.java 31 2005-09-04 15:57:49Z Adrian Smith $
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import com._17od.upm.AccountInformation;
import com._17od.upm.PasswordDatabase;
import com._17od.upm.ProblemReadingDatabaseFile;


public class DatabaseActions implements ActionListener {

    private MainWindow mainWindow;
    private PasswordDatabase database;

    
    	public DatabaseActions(MainWindow mainWindow) {
    		this.mainWindow = mainWindow;
    	}
	
    	
	public void actionPerformed(ActionEvent event) {
        try {
            if (event.getActionCommand() == MainWindow.NEW_DATABASE_TXT) {
                newDatabase();
            } else if (event.getActionCommand() == MainWindow.OPEN_DATABASE_TXT) {
                openDatabase();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainWindow, e.getMessage(), "Error...", JOptionPane.ERROR_MESSAGE);
            //TODO: Make this a better dialog that has a "show" button where you can see the full stack trace
        }
	}


    /**
     * This method asks the user for the name of a new database and then creates it.
     * If the file already exists then the user is asked if they'd like to overwrite it.
     * @throws IOException
     * @throws ProblemReadingDatabaseFile
     * @throws GeneralSecurityException
     */
    private void newDatabase() throws IOException, ProblemReadingDatabaseFile, GeneralSecurityException {
        
        File newDatabaseFile;
        boolean gotValidFile = false;
        do {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("New Password Database...");
            int returnVal = fc.showSaveDialog(mainWindow);
            
            if (returnVal != JFileChooser.APPROVE_OPTION) {
                return;
            }
            
            newDatabaseFile = fc.getSelectedFile();
            
            //Warn the user if the database file already exists
            if (newDatabaseFile.exists()) {
            		int i = JOptionPane.showConfirmDialog(mainWindow, "The file " + newDatabaseFile.getAbsolutePath() + 
            				" already exists.\nDo you want to overwrite it?", "File Already Exists...", JOptionPane.YES_NO_OPTION);
            		if (i == JOptionPane.YES_OPTION) {
                        gotValidFile = true;
            		}
            } else {
                gotValidFile = true;
            }
            
        } while (!gotValidFile);

        JPasswordField masterPassword;
        boolean passwordsMatch = false;
        do {
            
            //Get a new master password for this database from the user
            
            masterPassword = new JPasswordField("");
            JPasswordField confirmedMasterPassword = new JPasswordField("");
            JOptionPane pane = new JOptionPane(new Object[] {"Please enter a master password for your new database...",
                                                    masterPassword,
                                                    "Confirmation...",
                                                    confirmedMasterPassword},
                                                JOptionPane.QUESTION_MESSAGE,
                                                JOptionPane.OK_CANCEL_OPTION);
            JDialog dialog = pane.createDialog(mainWindow, "Master Password...");
            dialog.show();
    
            if (pane.getValue().equals(new Integer(JOptionPane.OK_OPTION))) {
            		if (!Arrays.equals(masterPassword.getPassword(), confirmedMasterPassword.getPassword())) {
                    JOptionPane.showMessageDialog(mainWindow, "The two passwords you entered don't match");
                } else {
                    passwordsMatch = true;
                }
            } else {
                return;
            }
            
        } while (passwordsMatch == false);

        if (newDatabaseFile.exists()) {
            newDatabaseFile.delete();
        }
        
        database = new PasswordDatabase(newDatabaseFile, masterPassword.getPassword());
        database.save();
        openDatabase(database);
        
    }
    
    
    private void openDatabase(PasswordDatabase database) {

    		//Enable the account buttons on the toolbar
    		mainWindow.getNewAccountButton().setEnabled(true);
    		mainWindow.getEditAccountButton().setEnabled(true);
    		mainWindow.getOptionsButton().setEnabled(true);

    		//Populate the listview
    		Iterator it = database.getAccounts().iterator();
    		while (it.hasNext()) {
    			AccountInformation account = (AccountInformation) it.next();
    			mainWindow.getAccountsModel().addElement(account.getAccountName());
    		}

    }
    
    
    private void openDatabase() throws IOException, ProblemReadingDatabaseFile, GeneralSecurityException {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Open Password Database...");
        int returnVal = fc.showOpenDialog(mainWindow);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File databaseFile = fc.getSelectedFile();
            
            JPasswordField masterPassword = new JPasswordField("");
            JOptionPane pane = new JOptionPane(new Object[] {"Please enter your master password",
                                                    masterPassword},
                                                JOptionPane.QUESTION_MESSAGE,
                                                JOptionPane.OK_CANCEL_OPTION);
            JDialog dialog = pane.createDialog(mainWindow, "Master Password...");
            dialog.show();

            if (pane.getValue().equals(new Integer(JOptionPane.OK_OPTION))) {
            		database = new PasswordDatabase(databaseFile, masterPassword.getPassword());
            		openDatabase(database);
            }
        }
        
    }
    
}
