/**
 * Show/hide shape-specific fields when the shape changes.
 */
function toggleShapeFields() {
    const shape = document.getElementById("shape").value;
    const circleFields = document.querySelectorAll(".circle-fields");
    const boxFields = document.querySelectorAll(".box-fields");
    if (shape === "CIRCLE_CULVERT") {
        circleFields.forEach((el) => (el.style.display = "flex"));
        boxFields.forEach((el) => (el.style.display = "none"));
    } else {
        circleFields.forEach((el) => (el.style.display = "none"));
        boxFields.forEach((el) => (el.style.display = "flex"));
    }
}

// Global variable to store calculation results
let calculationResults = null;
let calculationInputs = null;

/**
 * Collects form data, sends a POST request to the backend, and renders the response.
 */
async function calculate() {
    // Build the request payload
    const chainage = document.getElementById("chainage").value;
    const material = document.getElementById("material").value;
    const flowHeight = parseFloat(document.getElementById("flowHeight").value);
    const rainIntensity = parseFloat(
        document.getElementById("rainIntensity").value
    );
    const calculationArea = parseFloat(
        document.getElementById("calculationArea").value
    );
    const slope = parseFloat(document.getElementById("slope").value);
    const shape = document.getElementById("shape").value;
    const diameter = parseFloat(
        document.getElementById("structureDiameter").value
    );
    const width = parseFloat(document.getElementById("structureWidth").value);
    const height = parseFloat(document.getElementById("structureHeight").value);

    const requestBody = {
        chainage,
        material,
        flowHeight: isNaN(flowHeight) ? null : flowHeight,
        rainIntensity: isNaN(rainIntensity) ? null : rainIntensity,
        calculationArea: isNaN(calculationArea) ? null : calculationArea,
        slope: isNaN(slope) ? null : slope,
        shape,
        structureDiameter:
            shape === "CIRCLE_CULVERT" ? (isNaN(diameter) ? null : diameter) : null,
        structureWidth:
            shape === "BOX_CULVERT" ? (isNaN(width) ? null : width) : null,
        structureHeight:
            shape === "BOX_CULVERT" ? (isNaN(height) ? null : height) : null,
    };

    const resultContainer = document.getElementById("result");
    resultContainer.textContent = "Calculating...";

    try {
        const response = await fetch(
            "https://flow-master.onrender.com/simple/calculate/culvert",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(requestBody),
            }
        );

        if (!response.ok) {
            throw new Error("Failed to fetch calculation result.");
        }
        const data = await response.json();

        // Build a table of results
        let tableHtml = '<table class="result-table">';
        tableHtml += "<thead><tr><th>Field</th><th>Value</th></tr></thead><tbody>";
        const fieldsToDisplay = {
            centralAngle: "Central Angle",
            waterSpeed: "Water Speed",
            wettedPerimeter: "Wetted Perimeter",
            flowArea: "Flow Area",
            hydraulicRadius: "Hydraulic Radius",
            minAllowedSlope: "Min Allowed Slope",
            roughness: "Roughness",
            flowRate: "Flow Rate",
            requiredFlowRate: "Required Flow Rate",
            result: "Result",
        };
        Object.keys(fieldsToDisplay).forEach((key) => {
            const label = fieldsToDisplay[key];
            const value = data[key] != null ? data[key] : "-";
            tableHtml += `<tr><td>${label}</td><td>${value}</td></tr>`;
        });
        tableHtml += "</tbody></table>";
        resultContainer.innerHTML = tableHtml;
        
        // Store calculation results and inputs for report generation
        calculationResults = data;
        calculationInputs = requestBody;
        
        // Enable the Generate Report button
        const generateReportBtn = document.getElementById("generateReportBtn");
        generateReportBtn.disabled = false;
        
    } catch (error) {
        resultContainer.textContent = error.message;
        
        // Disable the Generate Report button on error
        const generateReportBtn = document.getElementById("generateReportBtn");
        generateReportBtn.disabled = true;
        calculationResults = null;
        calculationInputs = null;
    }
}

/**
 * Generates a PDF report by sending calculation data to the backend
 */
async function generateReport() {
    if (!calculationResults || !calculationInputs) {
        alert("No calculation data available. Please run a calculation first.");
        return;
    }
    
    const generateReportBtn = document.getElementById("generateReportBtn");
    const originalText = generateReportBtn.textContent;
    generateReportBtn.textContent = "Generating Report...";
    generateReportBtn.disabled = true;
    
    try {
        const reportData = {
            inputs: calculationInputs,
            results: calculationResults,
            timestamp: new Date().toISOString(),
            reportTitle: "FlowMaster Culvert Calculation Report"
        };
        
        const response = await fetch(
            "https://flow-master.onrender.com/api/generate-report",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(reportData),
            }
        );
        
        if (!response.ok) {
            throw new Error("Failed to generate report.");
        }
        
        // Get the PDF as blob and trigger download
        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        const timestamp = new Date().toISOString().replace(/[:.]/g, '-').slice(0, 19);
        a.href = url;
        a.download = `FlowMaster_Report_${timestamp}.pdf`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
        
    } catch (error) {
        alert("Error generating report: " + error.message);
    } finally {
        generateReportBtn.textContent = originalText;
        generateReportBtn.disabled = false;
    }
}

// Initialize shape fields on page load
document.addEventListener("DOMContentLoaded", () => {
    toggleShapeFields();
    
    // Ensure Generate Report button is initially disabled
    const generateReportBtn = document.getElementById("generateReportBtn");
    generateReportBtn.disabled = true;
});
