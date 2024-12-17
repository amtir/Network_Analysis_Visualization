# Network Analysis and Visualization Project

This project applies **data science** and **unsupervised learning techniques** to study networks (e.g., social connections, biological systems) by identifying their **most important elements** (called "prominent nodes"). An interactive **web application** is provided for analyzing and visualizing these networks.

## What This Project Does
- **Analyzes Networks**: Uses graph-based algorithms to extract meaningful insights.
- **Finds Key Nodes**: Identifies "prominent nodes," which are the most connected or influential parts of the network.
- **Visualizes Results**: Creates easy-to-understand visual representations of networks and their structure.

## Key Features
- **Algorithms for Analysis**:
  - K-Core Decomposition: Detects densely connected subgroups.
  - Biased Random Walk: Identifies key nodes based on connectivity patterns.
  - Core-Periphery Profile: Differentiates central (core) nodes from peripheral ones.
- **Learning Type**: Unsupervised learning methods (no labeled data needed).
- **Datasets**:
  - Supports various types of network data (e.g., `.csv`, `.json`).
  - Includes preloaded datasets like:
    - Zachary’s Karate Club (social network)
    - Bottlenose Dolphin Network (animal interactions)
    - Jazz Musicians Bands Network (collaborative network)

## Technologies Used
- **Data Science**:
  - Graph algorithms for unsupervised learning and analysis.
  - Stability and correctness testing across multiple datasets.
- **Web Development**:
  - **Frontend**: HTML5, CSS3, JavaScript, jQuery
  - **Backend**: Java EE, JSP, Servlet
  - **Communication**: AJAX, JSON
- **Server**: Apache Tomcat 7

## How to Use
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/network-analysis-visualization.git
   ```
2. **Deploy**:
   - Place the project in the `webapps` folder of Apache Tomcat.
   - Start the server and navigate to `http://localhost:8080/`.
3. **Analyze Networks**:
   - Upload datasets (e.g., `.csv` or `.json` files of networks).
   - Choose an algorithm (K-Core, Biased Random Walk, or Core-Periphery).
   - View and interpret the results with interactive graphs.

## Example Applications
- Understanding **social networks**: Who are the most connected or influential people?
- Studying **biological systems**: What proteins or cells are central to a process?
- Analyzing **collaborative networks**: Which contributors are most important?

## Who Should Use This?
- **Data Scientists**: To explore graph-based unsupervised learning techniques.
- **Students and Researchers**: Interested in network analysis or graph theory.
- **Professionals**: Analyzing relationships in social, biological, or technical systems.

## License
This project is licensed under the **MIT License**.
