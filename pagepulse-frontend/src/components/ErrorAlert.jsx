function ErrorAlert({ message }) {

    return (

        <div className="mt-6 bg-red-100 border border-red-300 text-red-700 rounded-lg p-4">

            {message}

        </div>

    );

}

export default ErrorAlert;