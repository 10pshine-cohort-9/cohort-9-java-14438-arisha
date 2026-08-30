import { useState } from "react";
import { useNavigate } from "react-router-dom";
import DashboardLayout from "../components/DashboardLayout";

function ContactToolsPage() {
    const [selectedFile, setSelectedFile] = useState(null);
    const [importMessage, setImportMessage] = useState("");
    const [error, setError] = useState("");

    const navigate = useNavigate();

    async function handleExportContacts() {
        const token = localStorage.getItem("token");

        try {
            const response = await fetch("/api/contacts/export", {
                headers: {
                    Authorization: "Bearer " + token,
                },
            });

            if (response.status === 401 || response.status === 403) {
                localStorage.removeItem("token");
                navigate("/");
                return;
            }

            if (!response.ok) {
                setError("Unable to export contacts");
                return;
            }

            const blob = await response.blob();
            const url = URL.createObjectURL(blob);

            const link = document.createElement("a");
            link.href = url;
            link.download = "contacts.csv";
            link.click();

            URL.revokeObjectURL(url);
            setError("");
        } catch {
            setError("Unable to connect to the server");
        }
    }

    async function handleImportContacts(event) {
        event.preventDefault();

        const token = localStorage.getItem("token");

        if (!selectedFile) {
            setImportMessage("Please select a CSV file");
            return;
        }

        const formData = new FormData();
        formData.append("file", selectedFile);

        try {
            const response = await fetch("/api/contacts/import", {
                method: "POST",
                headers: {
                    Authorization: "Bearer " + token,
                },
                body: formData,
            });

            if (response.status === 401 || response.status === 403) {
                localStorage.removeItem("token");
                navigate("/");
                return;
            }

            if (!response.ok) {
                setImportMessage("Unable to import contacts");
                return;
            }

            const message = await response.text();

            setImportMessage(message);
            setSelectedFile(null);
        } catch {
            setImportMessage("Unable to connect to the server");
        }
    }

    return (
        <DashboardLayout
            title="Contact Tools"
            subtitle="Import or export your contact data."
        >
            <section className="contact-tools-card">
    <div className="contact-tools-header">
        <div>
            <p className="contact-tools-eyebrow">
                CONTACT TOOLS
            </p>

            <h2>Import & Export</h2>

            <p className="contact-tools-description">
                Import contacts from a CSV file or export your contacts.
            </p>
        </div>

        <button
            className="contact-export-button"
            type="button"
            onClick={handleExportContacts}
        >
            Export Contacts
        </button>
    </div>

    <form
        className="contact-import-form"
        onSubmit={handleImportContacts}
    >
        <input
            className="contact-file-input"
            type="file"
            accept=".csv"
            onChange={(event) =>
                setSelectedFile(event.target.files[0])
            }
            required
        />

        <button
            className="contact-import-button"
            type="submit"
        >
            Import Contacts
        </button>
    </form>

    {importMessage && (
        <p className="contact-import-message">
            {importMessage}
        </p>
    )}
</section>
{error && <p>{error}</p>}
        </DashboardLayout>
    );
}

export default ContactToolsPage;