function LoadingSpinner() {

    return (

        <div className="text-center mt-10">

            <div className="animate-spin rounded-full h-12 w-12 border-b-4 border-blue-600 mx-auto"></div>

            <p className="mt-4 text-gray-600">
                Analyzing Website...
            </p>

        </div>

    );

}

export default LoadingSpinner;