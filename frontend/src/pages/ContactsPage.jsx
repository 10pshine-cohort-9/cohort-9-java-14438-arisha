import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import DashboardLayout from "../components/DashboardLayout";

function ContactsPage() {
    const [contacts, setContacts] = useState([]);
    const [error, setError] = useState("");

    const [editingId, setEditingId] = useState(null);
    const [editFirstName, setEditFirstName] = useState("");
    const [editLastName, setEditLastName] = useState("");
    const [editTitle, setEditTitle] = useState("");
    const [searchTerm, setSearchTerm] = useState("");

    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [totalContacts, setTotalContacts] = useState(0);
    const [activeSearchTerm, setActiveSearchTerm] = useState(""); 

    const [selectedFile, setSelectedFile] = useState(null);
    const [importMessage, setImportMessage] = useState("");
    const [refreshKey, setRefreshKey] = useState(0);

    const navigate = useNavigate();

    useEffect(() => {
        async function loadContacts() {
            const token = localStorage.getItem("token");

            if (!token) {
                navigate("/");
                return;
            }

            try {
                let url = "/api/contacts?page=" + currentPage + "&size=5";

                if (activeSearchTerm !== "") {
                    url = "/api/contacts/search?searchTerm=" + encodeURIComponent(activeSearchTerm) + "&page=" + currentPage + "&size=5";
                }

                const response = await fetch(url, {
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
                    setError("Unable to load contacts");
                    return;
                }

                const data = await response.json();
                setContacts(data.content);
                setTotalPages(data.totalPages);

                if (activeSearchTerm === "") {
                    setTotalContacts(data.totalElements);
                }
            } catch {
                setError("Unable to connect to the server");
            }
        }

        loadContacts();
    }, [navigate, currentPage, activeSearchTerm, refreshKey]);

       
    async function handleDeleteContact(contactId) {
        const token = localStorage.getItem("token");

        try {
            const response = await fetch("/api/contacts/" + contactId, {
                method: "DELETE",
                headers: {
                    Authorization: "Bearer " + token,
                },
            });

            if (!response.ok) {
                setError("Unable to delete contact");
                return;
            }

            const updatedContacts = contacts.filter(
                (contact) => contact.id !== contactId
            );

            setContacts(updatedContacts);
            setTotalContacts((value) => Math.max(0, value - 1));
            setError("");
        } catch {
            setError("Unable to connect to the server");
        }
    }  

    function startEditing(contact) {
        setEditingId(contact.id);
        setEditFirstName(contact.firstName);
        setEditLastName(contact.lastName);
        setEditTitle(contact.title || "");
    }

    async function handleUpdateContact(event) {
        event.preventDefault();

        const token = localStorage.getItem("token");

        try {
            const response = await fetch("/api/contacts/" + editingId, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: "Bearer " + token,
                },
                body: JSON.stringify({
                    firstName: editFirstName,
                    lastName: editLastName,
                    title: editTitle,
                }),
            });

            if (!response.ok) {
                setError("Unable to update contact");
                return;
            }

            const updatedContact = await response.json();

            const updatedContacts = contacts.map((contact) => {
                if (contact.id === editingId) {
                    return updatedContact;
                }

                 return contact;
            });

            setContacts(updatedContacts);
            setEditingId(null);
            setError("");
        } catch {
            setError("Unable to connect to the server");
        }
    }

    function handleSearch(event) {
        event.preventDefault();

        setCurrentPage(0);
        setActiveSearchTerm(searchTerm.trim());
    }

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

            setSearchTerm("");
            setActiveSearchTerm("");
            setCurrentPage(0);
            setRefreshKey((value) => value + 1);
        } catch {
            setImportMessage("Unable to connect to the server");
        }
    }

    return (
        <DashboardLayout
            title="Contacts Dashboard"
            subtitle="Manage and organize all your contacts from one place."
        >

            <section className="dashboard-stat-card">
                <p>Total Contacts</p>
                <h2>{totalContacts}</h2>
            </section>

            <button type="button" onClick={handleExportContacts}>
                Export Contacts
            </button>

            <h2>Import Contacts</h2>

            <form onSubmit={handleImportContacts}>
                <input
                    type="file"
                    accept=".csv"
                    onChange={(event) => setSelectedFile(event.target.files[0])}
                    required
                />

                <button type="submit">
                    Import Contacts
                </button>
            </form>

            {importMessage && <p>{importMessage}</p>}

            <form className="contact-search-form" onSubmit={handleSearch}>
                <input
                    className="contact-search-input"
                    type="text"
                    placeholder="Search contacts"
                    value={searchTerm}
                    onChange={(event) => setSearchTerm(event.target.value)}
                />

                <button className="contact-search-button" type="submit">
                    Search
                </button>
            </form>

            {error && <p>{error}</p>}

            {contacts.length === 0 && !error && (
                <p>No contacts found.</p>
            )}

            {contacts.map((contact) => (
                <div key={contact.id}>
                    {editingId === contact.id ? (
                        <form onSubmit={handleUpdateContact}>
                            <input
                                type="text"
                                value={editFirstName}
                                onChange={(event) => setEditFirstName(event.target.value)}
                                required
                            />

                            <input
                                type="text"
                                value={editLastName}
                                onChange={(event) => setEditLastName(event.target.value)}
                                required
                            />

                            <input
                                type="text"
                                value={editTitle}
                                onChange={(event) => setEditTitle(event.target.value)}
                            />

                            <button type="submit">Save</button>

                            <button
                                type="button"
                                onClick={() => setEditingId(null)}
                            >
                                Cancel
                            </button>
                        </form>
                    ) : (
                        <>
                            <h3>
                                {contact.firstName} {contact.lastName}
                            </h3>

                            <p>{contact.title}</p>

                            <button onClick={() => navigate("/contacts/" + contact.id)}>
                                View Details
                            </button>

                            <button onClick={() => startEditing(contact)}>
                                Edit
                            </button>

                            <button onClick={() => handleDeleteContact(contact.id)}>
                                Delete
                            </button>
                        </>
                    )}
                </div>
            ))}

            {totalPages > 0 && (
                <div>
                    <button
                        type="button"
                        onClick={() => setCurrentPage(currentPage - 1)}
                        disabled={currentPage === 0}
                    >
                        Previous
                    </button>

                    <span>
                        Page {currentPage + 1} of {totalPages}
                    </span>

                    <button type="button" onClick={() => setCurrentPage(currentPage + 1)} 
                    disabled={currentPage === totalPages - 1}
                    >
                        Next
                    </button>
                </div>
            )}
        </DashboardLayout>
    );
}

export default ContactsPage;