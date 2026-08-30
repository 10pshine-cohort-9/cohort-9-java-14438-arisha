
import { useNavigate } from "react-router-dom";
import DashboardLayout from "../components/DashboardLayout";
import { Fragment, useEffect, useState } from "react";

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

    const navigate = useNavigate();

    useEffect(() => {
        async function loadContacts() {
            const token = localStorage.getItem("token");

            if (!token) {
                navigate("/");
                return;
            }

            try {
                let url = "/api/contacts?page=" + currentPage + "&size=5&sort=firstName,asc&sort=lastName,asc";

                if (activeSearchTerm !== "") {
                    url = "/api/contacts/search?searchTerm=" + encodeURIComponent(activeSearchTerm) + "&page=" + currentPage +
                    "&size=5&sort=firstName,asc&sort=lastName,asc";
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
    }, [navigate, currentPage, activeSearchTerm]);

       
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

    function getAvatarClass(contactId) {
    const avatarClasses = [
        "contact-avatar-blue",
        "contact-avatar-purple",
        "contact-avatar-green",
        "contact-avatar-orange",
        "contact-avatar-pink",
    ];

    return avatarClasses[contactId % avatarClasses.length];
}

    return (
        <DashboardLayout
            title="Contacts Dashboard"
            subtitle="Manage and organize all your contacts from one place."
        >
            <div className="contacts-dashboard-content">

            <div className="dashboard-overview-grid">

            <section className="dashboard-stat-card">
                <div className="dashboard-stat-icon">
                    C
                </div>

                <div className="dashboard-stat-content">
                    <p>Total Contacts</p>
                    <h2>{totalContacts}</h2>
                    <span>Contacts in your address book</span>
                </div>
            </section>

            <button
                type="button"
                className="dashboard-action-card"
                onClick={() => navigate("/contacts/new")}
            >
                <div className="dashboard-action-icon">
                    +
                </div>

                <div className="dashboard-action-content">
                    <p>Add Contact</p>
                    <h3>New Contact</h3>
                    <span>Create a new contact</span>
                </div>
            </button>

            <button
                type="button"
                className="dashboard-action-card"
                onClick={() => navigate("/contact-tools")}
            >
                <div className="dashboard-action-icon">
                ⇅
                </div>

                <div className="dashboard-action-content">
                    <p>Contact Tools</p>
                    <h3>Import & Export</h3>
                    <span>Manage your CSV data</span>
                </div>
            </button>

        </div>
        <section className="contacts-directory">

    <div className="contacts-directory-header">
        <div>
            <p className="contacts-directory-eyebrow">
                YOUR CONTACTS
            </p>

            <h2>Contacts Directory</h2>
        </div>
    </div>
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

            {contacts.length > 0 && (
    <div className="contacts-table-card">
        <table className="contacts-table">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Title</th>
                    <th>Actions</th>
                </tr>
            </thead>

            <tbody>
                {contacts.map((contact, index) => {
    const currentLetter =
        contact.firstName.charAt(0).toUpperCase();

    const previousLetter =
        index > 0
            ? contacts[index - 1].firstName
                  .charAt(0)
                  .toUpperCase()
            : null;

    const showLetter =
        currentLetter !== previousLetter;

    return (
        <Fragment key={contact.id}>
            {showLetter && (
                <tr className="contact-letter-row">
                    <td colSpan="3">
                        {currentLetter}
                    </td>
                </tr>
            )}

            <tr>
                        {editingId === contact.id ? (
                            <td colSpan="3">
                                <form
                                    className="contact-edit-form"
                                    onSubmit={handleUpdateContact}
                                >
                                    <input
                                        type="text"
                                        value={editFirstName}
                                        onChange={(event) =>
                                            setEditFirstName(event.target.value)
                                        }
                                        required
                                    />

                                    <input
                                        type="text"
                                        value={editLastName}
                                        onChange={(event) =>
                                            setEditLastName(event.target.value)
                                        }
                                        required
                                    />

                                    <input
                                        type="text"
                                        value={editTitle}
                                        onChange={(event) =>
                                            setEditTitle(event.target.value)
                                        }
                                    />

                                    <div className="contact-edit-actions">
                                        <button
                                            className="contact-save-button"
                                            type="submit"
                                        >
                                            Save
                                        </button>

                                        <button
                                            className="contact-cancel-button"
                                            type="button"
                                            onClick={() => setEditingId(null)}
                                        >
                                            Cancel
                                        </button>
                                    </div>
                                </form>
                            </td>
                        ) : (
                            <>
                                <td>
                                    <div className="contact-name-cell">
                                        <div className={"contact-avatar " + getAvatarClass(contact.id)}>
                                            {contact.firstName.charAt(0).toUpperCase()}
                                        </div>

                                        <span>
                                            {contact.firstName} {contact.lastName}
                                        </span>
                                    </div>
                                </td>

                                <td>
                                    {contact.title || "—"}
                                </td>

                                <td>
                                    <div className="contact-actions">
                                        <button
                                            type="button"
                                            onClick={() =>
                                                navigate("/contacts/" + contact.id)
                                            }
                                        >
                                            View Details
                                        </button>

                                        <button
                                            type="button"
                                            onClick={() => startEditing(contact)}
                                        >
                                            Edit
                                        </button>

                                        <button
                                            type="button"
                                            onClick={() =>
                                                handleDeleteContact(contact.id)
                                            }
                                        >
                                            Delete
                                        </button>
                                    </div>
                                </td>
                            </>
                        )}
                    </tr>
                </Fragment>
                    );
})}
            </tbody>
        </table>
    </div>
)}

            {totalPages > 0 && (
    <div className="pagination">
        <button
            className="pagination-button"
            type="button"
            onClick={() => setCurrentPage(currentPage - 1)}
            disabled={currentPage === 0}
        >
            Previous
        </button>

        <span className="pagination-info">
            Page {currentPage + 1} of {totalPages}
        </span>

        <button
            className="pagination-button"
            type="button"
            onClick={() => setCurrentPage(currentPage + 1)}
            disabled={currentPage === totalPages - 1}
        >
            Next
        </button>
    </div>
)}
</section>
</div>
        </DashboardLayout>
    );
}

export default ContactsPage;