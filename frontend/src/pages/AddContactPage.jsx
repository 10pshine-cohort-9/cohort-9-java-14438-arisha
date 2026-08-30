import { useState } from "react";
import { useNavigate } from "react-router-dom";
import DashboardLayout from "../components/DashboardLayout";

function AddContactPage() {
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [title, setTitle] = useState("");
    const [error, setError] = useState("");

    const navigate = useNavigate();

    async function handleCreateContact(event) {
        event.preventDefault();

        const token = localStorage.getItem("token");

        if (!token) {
            navigate("/");
            return;
        }

        try {
            const response = await fetch("/api/contacts", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: "Bearer " + token,
                },
                body: JSON.stringify({
                    firstName: firstName,
                    lastName: lastName,
                    title: title,
                }),
            });

            if (response.status === 401 || response.status === 403) {
                localStorage.removeItem("token");
                navigate("/");
                return;
            }

            if (!response.ok) {
                setError("Unable to create contact");
                return;
            }

            setError("");
            navigate("/contacts");
        } catch {
            setError("Unable to connect to the server");
        }
    }

    return (
        <DashboardLayout
            title="Add Contact"
            subtitle="Create a new contact for your address book."
        >
            <section className="add-contact-card">
    <div className="add-contact-header">
        <p className="add-contact-eyebrow">
            NEW CONTACT
        </p>

        <h2>Contact Information</h2>

        <p>
            Enter the basic details for your new contact.
        </p>
    </div>

    <form
        className="add-contact-form"
        onSubmit={handleCreateContact}
    >
        <div className="add-contact-group">
            <label>First Name</label>

            <input
                type="text"
                value={firstName}
                onChange={(event) => setFirstName(event.target.value)
                }
                required
            />
        </div>

        <div className="add-contact-group">
            <label>Last Name</label>

            <input
                type="text"
                value={lastName}
                onChange={(event) => setLastName(event.target.value)
                }
                required
            />
        </div>

        <div className="add-contact-group">
            <label>Title</label>

            <input
                type="text"
                value={title}
                onChange={(event) => setTitle(event.target.value)
                }
            />
        </div>

        <div className="add-contact-actions">
            <button
                className="add-contact-button"
                type="submit"
            >
                Add Contact
            </button>

            <button
                className="add-contact-cancel-button"
                type="button"
                onClick={() => navigate("/contacts")}
            >
                Cancel
            </button>
        </div>
    </form>

    {error && (
        <p className="add-contact-error">
            {error}
        </p>
    )}
</section>
        </DashboardLayout>
    );
}

export default AddContactPage;