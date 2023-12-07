package assignment2;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class AdminView extends UserView{
	
	private static final long serialVersionUID = 1L;
	
	private static AdminView instance;
	
	private static JTree tree;
	private static JTextField userIdTextField;
	private static JTextField groupIdTextField;
	
	private String currentUserNode;
	private String currentGroupNode;
	private DefaultMutableTreeNode currentGroupTreeNode;
	
	public AdminView() {
		TwitterUserManager.getInstance();
		
		setTitle("Twitter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);

        // Create the left side panel for the TreeView
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        tree = new JTree(root);
        JScrollPane treeScrollPane = new JScrollPane(tree);

        // Create the right side panel for other components
        JPanel rightPanel = new JPanel(new BorderLayout());

        // Create the top-right panel
        JPanel topRightPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        userIdTextField = new JTextField();
        JButton addUserButton = new JButton("Add User");
        groupIdTextField = new JTextField();
        JButton addGroupButton = new JButton("Add Group");
        topRightPanel.add(new JLabel("User ID"));
        topRightPanel.add(userIdTextField);
        topRightPanel.add(addUserButton);
        topRightPanel.add(new JLabel("Group ID"));
        topRightPanel.add(groupIdTextField);
        topRightPanel.add(addGroupButton);

        // Create the center panel
        JButton openUserViewButton = new JButton("Open User View");
        rightPanel.add(topRightPanel, BorderLayout.NORTH);
        rightPanel.add(openUserViewButton, BorderLayout.CENTER);

        // Create the bottom-right panel with a 2x2 grid
        JPanel bottomRightPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JButton showUserTotalButton = new JButton("Show User Total");
        JButton showGroupTotalButton = new JButton("Show Group Total");
        JButton showMessageTotalButton = new JButton("Show Message Total");
        JButton showPositivePercentageButton = new JButton("Show Positive Percentage");
        JButton showValidUserGroupButton = new JButton("Show User and Group Validity");
        JButton showFindLastUpdatedButton = new JButton("Show Last Updated User");
        bottomRightPanel.add(showUserTotalButton);
        bottomRightPanel.add(showGroupTotalButton);
        bottomRightPanel.add(showMessageTotalButton);
        bottomRightPanel.add(showPositivePercentageButton);
        bottomRightPanel.add(showValidUserGroupButton);
        bottomRightPanel.add(showFindLastUpdatedButton);

        rightPanel.add(bottomRightPanel, BorderLayout.SOUTH);
    
        // Use GridBagLayout to position the left and right panels
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5; 
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(treeScrollPane, gbc);

        gbc.weightx = 0.1; 
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(rightPanel, gbc);

        // Button click handling
        tree.addTreeSelectionListener(e-> valueChanged(e));
        addUserButton.addActionListener(e -> handleAddUserButton(currentGroupNode, userIdTextField.getText()));
        addGroupButton.addActionListener(e -> handleAddGroupButton(groupIdTextField.getText()));
        openUserViewButton.addActionListener(e -> new UserView().handleOpenUserViewButton(currentUserNode));
        showUserTotalButton.addActionListener(e -> new StatisticVisitor().getStatistics(new TotalUserButton()));
        showGroupTotalButton.addActionListener(e -> new StatisticVisitor().getStatistics(new TotalGroupButton()));
        showMessageTotalButton.addActionListener(e -> new StatisticVisitor().getStatistics(new TotalTweetButton()));
        showPositivePercentageButton.addActionListener(e -> new StatisticVisitor().getStatistics(new PositivePercentageButton()));
        showValidUserGroupButton.addActionListener(e -> new StatisticVisitor().getStatistics(new ValidUserGroupButton()));
        showFindLastUpdatedButton.addActionListener(e -> new StatisticVisitor().getStatistics(new FindLastUpdatedButton()));
        
	}
    
	public static AdminView getInstance() {
		
		if(instance == null) {
			instance = new AdminView();
		}
		
		return instance;
	}
	
    private void valueChanged(TreeSelectionEvent e) {
        TreePath selectedPath = e.getNewLeadSelectionPath();
        
        if (selectedPath != null) {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
            String nodeName = selectedNode.getUserObject().toString();
            if(nodeName!="Members go here!" && !nodeName.contains("Group") && !nodeName.equals("Root")) {
            	
                currentUserNode = nodeName;
                
            } else {
            	
            	if(nodeName.contains("Group")) {
            		
            		currentGroupNode = nodeName;
            		currentGroupTreeNode = selectedNode;
            		
            	} else {
            		
            		if(nodeName.equals("Root")) {
            			
            			currentGroupNode = null;
            			currentGroupTreeNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
            		}
            	}
            }
        }
    }
	
	private void handleAddUserButton(String groupname, String username) {
		
		if(username.length()<1) {
			System.out.println("Username must be at least one character");
			return;
		}
		
		if(TwitterUserManager.userExists(new UserID(username))) {
			System.out.println("This user already exists.");
			return;
		}
		
		if(username.equals("Root") || username.contains("Group")) {
			System.out.println("Usernames must not be 'Root' or contain 'Group'");
			return;
		}
		
		 DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		
        if(TwitterUserManager.addUser(groupname, username) != null) {
        	
        	if(this.currentGroupNode == null) {
        		
               
                DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
                root.add(new DefaultMutableTreeNode(username));
                model.reload(root);
                
                userIdTextField.setText("");
                
        	} else { //add to a group
        		
        		currentGroupTreeNode.add(new DefaultMutableTreeNode(username));
        		model.reload(currentGroupTreeNode);
        		userIdTextField.setText("");
        	}
        }	
    }

    private void handleAddGroupButton(String groupname) { 
    	
    	if(TwitterUserManager.groupExists(new GroupID(groupname))) {
    		System.out.println("This group already exists.");
			return;
    	}
    	
        if(TwitterUserManager.addGroup(groupname, currentGroupNode) != null) {
        	
        	DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        	DefaultMutableTreeNode placeholder = new DefaultMutableTreeNode("Members go here!");
        	
        	if(currentGroupNode != null) {
        		
        		DefaultMutableTreeNode grouproot =new DefaultMutableTreeNode(groupname + " Group");
        		grouproot.add(placeholder);
        		currentGroupTreeNode.add(grouproot);
        		model.reload(currentGroupTreeNode);
        		
        	} else {
        		
        		DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
                DefaultMutableTreeNode grouproot = new DefaultMutableTreeNode(groupname + " Group");
                
                grouproot.add(placeholder);
                root.add(grouproot);
                model.reload(root);
        	}
            
            System.out.println("Group " + groupname + " added.");
            
        }
        groupIdTextField.setText("");
    }
}
